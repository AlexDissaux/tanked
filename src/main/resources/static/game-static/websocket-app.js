let ws = {};

(function(ws) {
    let socket = null;
    let stompClient = null;
    let sessionId = null;
    let handlers = {};

    ws.start = function start() {
        socket = new SockJS('/websocket');
        stompClient = Stomp.over(socket);
        stompClient.debug = () => {};
        stompClient.connect({}, onConnected, onError);
    };

    function onConnected() {
        // * Get the sessionId to identify the identity of the user
        // socket._transport.url in the form : "ws://host:port/websocket/963/sessionId/websocket"
        // splice : Get the one before last
        sessionId = socket._transport.url.split('/').splice(-2,1)[0];

        // * Subscribe to webhook channels
        stompClient.subscribe('/user/queue/onlyforme', onMessage);
        stompClient.subscribe('/game/game-' + gameId, onMessage);

        console.log("Connected to WebSocket !");
    }

    function onError(error) {
        // display the error's message header:
        console.log("Error : " + error);

        if('STOP' in handlers)
            handlers['STOP']();

        console.log("Reconnecting...");
        window.setTimeout(function() {
            start();
        }, 2500);
    }

    function onMessage(message) {
       let body = JSON.parse(message.body);
       if('type' in body) {
           if(body.type in handlers) {
               handlers[body.type].apply(undefined, body.payload);
           } else {
               throw "Message with unhandled type " + body.type;
           }
       } else {
           throw "Malformed message : not handled";
       }
    }

    ws.whoami = function whoami() {
        return sessionId;
    };

    ws.handle = function handleWS(messageType, handler) {
        if(handler instanceof Function) {
            handlers[messageType] = handler;
        } else {
            throw "Handler for " +messageType +" is not a function";
        }
    };

    ws.send = function sendWS(path, payload) {
        stompClient.send('/app/' + path, {}, JSON.stringify(payload));
    };
})(ws);
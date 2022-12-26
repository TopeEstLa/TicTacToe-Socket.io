import { useCallback, useEffect, useState } from "react";
import * as io from "socket.io-client";

export const useSocket = () => {
    const [socket, setSocket] = useState();
    const [isConnected, setConnected] = useState(false);

    const joinGame = useCallback(
        (gameId, playername) => {
            socket.emit("join", {
                gameId: gameId,
                playerName: playername,
            });
        },
        [socket]
    );


    useEffect(() => {
        const s = io("localhost:8085", {
            reconnection: false,
        });
        setSocket(s);
        s.on("connect", () => setConnected(true));
        s.on("disconnect", () => setConnected(false));
    }, []);

    return { isConnected, joinGame };
};
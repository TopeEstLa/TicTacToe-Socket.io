import * as React from "react";
import {useEffect, useState} from "react";
import {useSocket} from "../service/useSocket";
import {postCreateGame, getAvailableGame} from "../service/api";
// @ts-ignore
import * as io from "socket.io-client";

interface Game {
    id: number;
    status: string;
    created: string;
    board: string;
    player1: string;
    player2: string;
    lastPlayer: string;
    winner: string;
}
const s = io("localhost:8085", {
    reconnection: false,
});
const HomeComposents: React.FC<{ updateInGameStatus:  React.Dispatch<React.SetStateAction<boolean>>}> = ({ updateInGameStatus }) => {
    const [data, setData] = useState<Game[]>([]);
    const {joinGame} = useSocket();
    const [username, setUsername] = useState("");

    useEffect(() => {
        async function fetchData() {
            try {
                const data = await getAvailableGame();
                setData(data);
            } catch (error) {
                console.error(error);
            }
        }

        fetchData()
    }, []);

    async function createGame() {
        try {
            const data = await postCreateGame();
            joinGame(data.id);
        } catch (error) {
            console.error(error);
        }
    }

    function joinGameClick(id: number) {
        if (username == "") {
            alert("Please enter a username");
            return;
        }

        joinGame(id, username);
        updateInGameStatus(true);
    }


    return (
        <div>
            <input
                type="text"
                required
                placeholder="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />

            <h1>Hello soit tu join une game soit tu en crée une</h1>
            <p>game dispo</p>
            {data.map(item => (
                <>
                    <div key={item.id}>{item.id}</div>
                    <button onClick={() => joinGameClick(item.id)}>Rejoindre
                    </button>
                </>
            ))}
            <p>Crée une game</p>
            <button onClick={() => createGame()}>Crée une game</button>
        </div>
    );
}
export default HomeComposents;

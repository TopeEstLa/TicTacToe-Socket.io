import {useEffect, useState} from "react";
import axios from 'axios';
import "../public/home.css";
import {timeStampConverter} from "../util/timeUtils";

export const HomeComponent = ({socketIO, username, requestGameId, setRequestGameId}) => {

    const [data, setData] = useState([]);
    const [localGameId, setLocalGameId] = useState("");

    async function fetchData() {
        const response = await axios.get('http://127.0.0.1:8080/api/game/available');
        setData(response.data);
    }


    const checkJoinSpecial = (e) => {
        e.preventDefault();
        if (localGameId === "") {
            alert("game id is needed");
        } else {
            joinGame(localGameId);
        }
    };

    function joinGame(id) {
        setLocalGameId(id)
        setRequestGameId(id)
        socketIO.emit("join_game", {gameId: id, playerName: username});
    }

    async function createGame() {
        const response = await axios.post("http://127.0.0.1:8080/api/game/create")
        joinGame(response.data.id);
    }

    useEffect(() => {
        fetchData();
    }, []);


    return (
        <div>
            <div className="make_game">
                <h1>Crée une game</h1>
                <button onClick={() => createGame()}>Lancez une partie</button>
            </div>
            <div className="join_game">
                <form onSubmit={checkJoinSpecial}>
                    <h1>Rejoindre une game</h1>
                    <input
                        type="text"
                        required
                        placeholder="id de partie"
                        value={localGameId}
                        onChange={(e) => setLocalGameId(e.target.value)}
                    />
                    <input type="submit" value="Lancer la game"/>
                </form>
            </div>
            <div className="list_games">
                <h1>Parti disponible</h1>
                {data.map(item => (
                    <>
                        <p className="games">{item.id} | {timeStampConverter(item.created)} | {item.status}</p>
                        <button onClick={() => joinGame(item.id)}>Rejoindre</button>
                    </>
                ))}
            </div>
        </div>
    );
}
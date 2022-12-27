import {useEffect, useState} from "react";
import axios from 'axios';
import "../public/home.css";
import 'react-toastify/dist/ReactToastify.css';




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
        <div className="box_container">
            <div className="box">
                <h1>Crée une game</h1>
                <button className="pute" onClick={() => createGame()}>Lancez une partie</button>
            </div>
            <div className="box">
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
            <div className="box">
                <h1>Parti disponible</h1>
                <ul>
                    {data.map((game) => (
                        <li key={game.id}>
                            <span>id: {game.id}, Adversaire {game.player1}</span>
                            <button className="pute" onClick={() => joinGame(game.id)}>Rejoindre</button>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}
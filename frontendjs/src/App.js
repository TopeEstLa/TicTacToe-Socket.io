import './public/App.css';
import io from "socket.io-client";
import {LoginComponent} from "./components/LoginComponent";
import {HomeComponent} from "./components/HomeComponent";

import {useEffect, useState} from "react";
import {PreStartComponent} from "./components/PreStartComponent";
import {GameComponent} from "./components/GameComponent";
import { ToastContainer, toast } from 'react-toastify';

const socket = io.connect("http://localhost:8085");

socket.on("connect", () => {
    console.log("Connected to server");
});


function App() {
    let ind = 0;
    const [username, setUsername] = useState("");
    const [isLoggedIn, setLogin] = useState(false);
    const [isInGame, setInGame] = useState(false);
    const [isGameStarted, setGameStarted] = useState(false);
    const [isGamePreStart, setGamePreStart] = useState(false);
    const [gameId, setGameId] = useState(false);
    const [requestGameId, setRequestGameId] = useState(false);
    const [gameData, setGameData] = useState("");

    function handleJoinSuccess() {
        socket.on("join_callback", (data) => {
            console.log(data)
            if (data.status === "SUCCESS") {
                if (!isInGame) {
                    toast.success('🦄 Parti rejoins !', {
                        position: "top-right",
                        autoClose: 5000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: false,
                        draggable: true,
                        progress: undefined,
                        theme: "light",
                    });

                    setInGame(true);
                    setGameId(data.gameId);
                    setRequestGameId("");
                }
            } else {
                toast.error(data.status, {
                    position: "top-right",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "light",
                });
            }
        });
    }

    function fetchWaitingForPlayer() {
        socket.on("game_soon_start", (data) => {
            if (!isGameStarted) {
                setGamePreStart(true);
            }
        });
    }

    function fetchGameStart() {
        socket.on("game_start", (data) => {
            if (!isGameStarted) {
                setGameData(data);
                setGameStarted(true);
                setGamePreStart(false);
            }
        });
    }

    useEffect(() => {
        ind++;
        if (ind == 1) {
            handleJoinSuccess();
            fetchWaitingForPlayer();
            fetchGameStart();
        }
    }, []);

    return (
        <div>
            {!isLoggedIn ? (
                <LoginComponent username={username} setUsername={setUsername} setLogin={setLogin}/>
            ) : (
                <div>
                    {!isInGame ? (
                        <HomeComponent socketIO={socket} username={username} requestGameId={requestGameId}
                                       setRequestGameId={setRequestGameId}/>
                    ) : (
                        <div>
                            {!isGameStarted ? (
                                <div>
                                    {!isGamePreStart ? (
                                        <div>on attend un pélot</div>
                                    ) : (
                                        <PreStartComponent/>
                                    )}
                                </div>
                            ) : (
                                <GameComponent socketIO={socket} username={username} setInGame={setInGame} setGameStarted={setGameStarted} setGamePreStart={setGamePreStart}
                                               setGameId={setGameId}
                                               setGameData={setGameData} gameData={gameData} />
                            )}
                        </div>
                    )}
                </div>
            )};
            <ToastContainer />
        </div>
    );
}

export default App;

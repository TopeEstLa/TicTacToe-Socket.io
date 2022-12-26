import React, {useState} from 'react';
import './App.css';
import HomeComposents from "./components/HomeComposents";
import GameComposents from "./components/GameComposents";


function App() {
    const [isInGame, setInGame] = useState(false);
    return (
        <div>
            {isInGame ? <GameComposents/> : <HomeComposents updateInGameStatus={setInGame}/>}
        </div>
    );
}

export default App;

import {useEffect, useState} from "react";
import "../public/game.css";
import styled from "styled-components";
import {toast} from "react-toastify";

const RowContainer = styled.div`
  width: 100%;
  display: flex;
`;

const Cell = styled.div`
  width: 13em;
  height: 9em;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 20px;
  cursor: pointer;
  border-top: ${({borderTop}) => borderTop && "3px solid #19d313"};
  border-left: ${({borderLeft}) => borderLeft && "3px solid #19d313"};
  border-bottom: ${({borderBottom}) => borderBottom && "3px solid #19d313"};
  border-right: ${({borderRight}) => borderRight && "3px solid #19d313"};
  transition: all 270ms ease-in-out;

  &:hover {
    background-color: #19d313;
  }
`;

const X = styled.span`
  font-size: 100px;
  color: #19d313;

  &::after {
    content: "X";
  }
`;

const O = styled.span`
  font-size: 100px;
  color: #41f114;

  &::after {
    content: "O";
  }
`;

export const GameComponent = ({socketIO, username, setInGame, setGameStarted, setGamePreStart, setGameId, setGameData, gameData}) => {

    const [board, setBoard] = useState([[0, 0, 0], [0, 0, 0], [0, 0, 0]]);
    const [whoPlay, setWhoPlay] = useState("x");

    function onUpdateBoard() {
        socketIO.on("game_update", (data) => {
            updateInfo(data);
        });
    }

    function handleWin() {
        socketIO.on("game_win", (data) => {
            data.winner === username ? toast.success("🏆 You win") : toast.error("💩 You lose");
            setInGame(false)
            setGameStarted(false)
            setGamePreStart(false)
            setGameId(false)
            setGameData("")
        });
    }

    function handleMoveCallBack() {
        socketIO.on("game_callback", (data) => {
            toast.error(data.status, {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: false,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        });
    }

    function updateInfo(data) {
        console.log(data)
        let split = data.board.split(";");
        let board = []
        board.push(split[0].split(""));
        board.push(split[1].split(""));
        board.push(split[2].split(""));
        setBoard(board);
        setWhoPlay(data.nextPlayer);
    }

    useEffect(() => {
        updateInfo(gameData);
        onUpdateBoard();
        handleWin();
        handleMoveCallBack();
    }, []);

    function sendMove(moveX, movY) {
        if (whoPlay === username) {
            socketIO.emit("game_move", {gameId: gameData.gameId, player: username, x: moveX, y: movY});
        }
    }

    function format(entry) {
        if (entry === 0) {
            return null;
        } else if (entry === "1") {
            return  <O/>;
        } else if (entry === "2") {
            return <X/>;
        }
    }

    return (
        <div>
            <p>Le tour de {whoPlay}</p>
            {board.map((row, rowIdx) => {
                return (
                    <RowContainer>
                        {row.map((column, columnIdx) => (
                            <Cell
                                borderRight={columnIdx < 2}
                                borderLeft={columnIdx > 0}
                                borderBottom={rowIdx < 2}
                                borderTop={rowIdx > 0}
                                onClick={() =>
                                    sendMove(columnIdx, rowIdx)
                                }
                            >
                                {format(column)}
                            </Cell>
                        ))}
                    </RowContainer>
                );
            })
            };

        </div>
    );
}

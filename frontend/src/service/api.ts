import axios from 'axios';

const apiUrl = 'http://127.0.0.1:8080/api';

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

export const getAvailableGame = async (): Promise<Game[]> => {
    try {
        const response = await axios.get<Game[]>(`${apiUrl}/game/available`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const postCreateGame = async (): Promise<Game> => {
    try {
        const response = await axios.post<Game>(`${apiUrl}/game/create`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}
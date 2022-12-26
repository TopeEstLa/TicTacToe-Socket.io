export const timeStampConverter = (time) => {
    const date = new Date(time);
    const day = date.getDay();
    const month = date.getMonth();
    const minute = date.getMinutes();
    const hour = date.getHours();
    return `${day}/${month} at ${hour}:${minute}`;
};
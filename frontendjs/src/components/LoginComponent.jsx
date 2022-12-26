import "../public/login.css";

export const LoginComponent = ({username, setUsername, setLogin}) => {
    const checkForLogin = (e) => {
        e.preventDefault();
        if (username === "") {
            alert("fill the required fields");
        } else {
            setLogin(true);
        }
    };

    return (
        <div className="login_root">
            <form className="login_form" onSubmit={checkForLogin}>
                <input
                    type="text"
                    required
                    placeholder="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input type="submit" value="Login" />
            </form>
        </div>
    );
};
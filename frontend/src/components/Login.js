import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const TOKEN_KEY = "auth_token";
const API_BASE_URL = "http://localhost:8081/api";

export default function Login() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  // Redirect if already logged in
  useEffect(() => {
    const token = localStorage.getItem(TOKEN_KEY);
    if (token) {
      navigate("/");
    }
  }, [navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const res = await fetch(`${API_BASE_URL}/auth/signin`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          username: username.trim(),
          password: password.trim()
        })
      });

      if (res.status === 401) {
        throw new Error("Invalid username or password");
      }

      if (!res.ok) {
        throw new Error("Login failed");
      }


    const token = await res.text();
    localStorage.setItem(TOKEN_KEY, token);


      navigate("/");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="card p-4" style={{ maxWidth: 400, margin: "0 auto" }}>
      <h2 className="mb-3">Login</h2>

      {error && <div className="alert alert-danger">{error}</div>}

      <form onSubmit={handleSubmit}>
        <input
          className="form-control mb-2"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />

        <input
          type="password"
          className="form-control mb-3"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button
          className="btn btn-primary w-100"
          disabled={!username || !password}
        >
          Login
        </button>
      </form>
    </div>
  );
}
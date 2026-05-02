import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";

const API_URL = "http://localhost:8080/api/patients";
const TOKEN_KEY = "auth_token";

function PatientList() {
  const [patients, setPatients] = useState([]);
  const [lastName, setLastName] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  const loadPatients = async () => {
    setLoading(true);
    setError(null);

    const token = localStorage.getItem(TOKEN_KEY);

    if (!token) {
      navigate("/login");
      return;
    }

    try {
      const response = await axios.get(API_URL, {
        headers: {
          Authorization: `Bearer ${token}`
        },
        withCredentials: true,
        params: lastName ? { lastName } : {}
      });

      setPatients(response.data);
    } catch (err) {
      if (err.response?.status === 401) {
        localStorage.removeItem(TOKEN_KEY);
        navigate("/login");
      } else {
        console.error("Error loading patients:", err);
        setError("Failed to load patients");
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPatients();
  }, []);

  return (
    <div>
      <h2>Patients</h2>

      <div className="mb-3 d-flex gap-2">
        <input
          className="form-control"
          placeholder="Filter by last name"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />

        <button className="btn btn-secondary" onClick={loadPatients}>
          Search
        </button>

        <Link to="/patients/new" className="btn btn-success">
          New Patient
        </Link>
      </div>

      {loading && <p>Loading...</p>}
      {error && <p className="text-danger">{error}</p>}

      {!loading && !error && patients.length === 0 && (
        <p>No patients found.</p>
      )}

      {!loading && !error && patients.map((p) => (
        <div
          key={p.id}
          className="border rounded p-3 mb-2 d-flex justify-content-between align-items-center"
        >
          <div>
            <strong>{p.firstName} {p.lastName}</strong>
            <div className="text-muted">
              Date of Birth: {p.dateOfBirth}
            </div>
          </div>

          <div className="d-flex gap-2">
            <Link
              to={`/patients/${p.id}`}
              className="btn btn-sm btn-info"
            >
              View
            </Link>

            <Link
              to={`/patients/edit/${p.id}`}
              className="btn btn-sm btn-warning"
            >
              Edit
            </Link>
          </div>
        </div>
      ))}
    </div>
  );
}

export default PatientList;

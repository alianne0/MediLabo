import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

// Gateway entry point
const API_URL = "http://localhost:8080/api/patients";

function PatientList() {
  const [patients, setPatients] = useState([]);
  const [lastName, setLastName] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loadPatients = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await axios.get(API_URL, {
        params: lastName ? { lastName } : {}
      });

      setPatients(response.data);
    } catch (err) {
      console.error("Error loading patients:", err);
      setError("Failed to load patients");
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

      {!loading && !error && (
        <table className="table table-striped">
          <thead>
            <tr>
              <th>Name</th>
              <th>Date of Birth</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {patients.map((p) => (
              <tr key={p.id}>
                <td>
                  {p.firstName} {p.lastName}
                </td>
                <td>{p.dateOfBirth}</td>
                <td>
                  <Link
                    to={`/patients/${p.id}`}
                    className="btn btn-sm btn-info me-2"
                  >
                    View
                  </Link>
                  <Link
                    to={`/patients/edit/${p.id}`}
                    className="btn btn-sm btn-warning"
                  >
                    Edit
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default PatientList;

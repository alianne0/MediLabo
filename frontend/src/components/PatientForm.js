import { useEffect, useState, useCallback } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";

const API_URL = "http://localhost:8080/api/patients";
const TOKEN_KEY = "auth_token";

function PatientForm() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [patient, setPatient] = useState({
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    gender: "",
    address: "",
    phone: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  /* Load existing patient when editing */
  const loadPatient = useCallback(async () => {
    if (!id) return;

    setLoading(true);
    setError(null);

    try {
      const response = await axios.get(`${API_URL}/${id}`, {
        headers: { Authorization: `Bearer ${localStorage.getItem(TOKEN_KEY)}` },
      });
      setPatient(response.data);
    } catch (err) {
      console.error("Error loading patient:", err);
      setError("Failed to load patient");
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadPatient();
  }, [loadPatient]);

  const handleChange = (e) => {
    setPatient({
      ...patient,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setLoading(true);
    setError(null);

    try {
      const token = localStorage.getItem(TOKEN_KEY);
      if (!token) {
        navigate("/login");
        return;
      }

      if (id) {
        await axios.put(`${API_URL}/${id}`, patient, {
          headers: { Authorization: `Bearer ${token}` },
        });
      } else {
        await axios.post(API_URL, patient, {
          headers: { Authorization: `Bearer ${token}` },
        });
      }

      navigate("/patients");
    } catch (err) {
      console.error("Error saving patient:", err);
      setError("Failed to save patient");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2>{id ? "Edit Patient" : "Create Patient"}</h2>

      {error && <p className="text-danger">{error}</p>}
      {loading && <p>Loading...</p>}

      <form onSubmit={handleSubmit}>
        <input
          className="form-control mb-2"
          name="firstName"
          placeholder="First Name"
          value={patient.firstName}
          onChange={handleChange}
        />

        <input
          className="form-control mb-2"
          name="lastName"
          placeholder="Last Name"
          value={patient.lastName}
          onChange={handleChange}
        />

        <input
          type="date"
          className="form-control mb-2"
          name="dateOfBirth"
          value={patient.dateOfBirth}
          onChange={handleChange}
        />

        <input
          className="form-control mb-2"
          name="gender"
          placeholder="Gender"
          value={patient.gender}
          onChange={handleChange}
        />

        <input
          className="form-control mb-2"
          name="address"
          placeholder="Address"
          value={patient.address}
          onChange={handleChange}
        />

        <input
          className="form-control mb-3"
          name="phone"
          placeholder="Phone"
          value={patient.phone}
          onChange={handleChange}
        />

        <button className="btn btn-primary" disabled={loading}>
          {id ? "Update Patient" : "Create Patient"}
        </button>
      </form>
    </div>
  );
}

export default PatientForm;

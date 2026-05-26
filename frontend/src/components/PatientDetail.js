import { useEffect, useState, useCallback } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import axios from "axios";

const API_URL = "http://localhost:8080/api/patients";
const NOTES_URL = "http://localhost:8080/api/notes";
const RISK_URL = "http://localhost:8080/api/risk";
const TOKEN_KEY = "auth_token";

function PatientDetail() {
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
  const [showNotes, setShowNotes] = useState(false);
  const [notes, setNotes] = useState([]);
  const [notesLoading, setNotesLoading] = useState(false);
  const [notesError, setNotesError] = useState(null);
  const [newNote, setNewNote] = useState("");
  const [savingNote, setSavingNote] = useState(false);
  const [riskLevel, setRiskLevel] = useState(null);
  const [riskLoading, setRiskLoading] = useState(false);
  const [riskError, setRiskError] = useState(null);

  const loadPatient = useCallback(async () => {
    if (!id) return;

    setLoading(true);
    setError(null);

    const token = localStorage.getItem(TOKEN_KEY);
    if (!token) {
      navigate("/login");
      return;
    }

    try {
      const response = await axios.get(`${API_URL}/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setPatient(response.data);
    } catch (err) {
      if (err.response?.status === 401 || err.response?.status === 403) {
        localStorage.removeItem(TOKEN_KEY);
        navigate("/login");
      } else {
        console.error("Error loading patient:", err);
        setError("Failed to load patient");
      }
    } finally {
      setLoading(false);
    }
  }, [id, navigate]);

  const loadRisk = useCallback(async () => {
    if (!id) return;
    setRiskLoading(true);
    setRiskError(null);
    const token = localStorage.getItem(TOKEN_KEY);
    try {
      const response = await axios.get(`${RISK_URL}/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setRiskLevel(response.data.riskLevel);
    } catch (err) {
      console.error("Error loading risk:", err);
      setRiskError("Unable to load risk assessment.");
    } finally {
      setRiskLoading(false);
    }
  }, [id]);

  const loadNotes = useCallback(async () => {
    setNotesLoading(true);
    setNotesError(null);
    const token = localStorage.getItem(TOKEN_KEY);
    try {
      const response = await axios.get(`${NOTES_URL}/patient/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setNotes(response.data);
    } catch (err) {
      console.error("Error loading notes:", err);
      setNotesError("Failed to load notes.");
    } finally {
      setNotesLoading(false);
    }
  }, [id]);

  const handleOpenNotes = () => {
    setShowNotes(true);
    loadNotes();
  };

  const handleAddNote = async (e) => {
    e.preventDefault();
    if (!newNote.trim()) return;
    setSavingNote(true);
    const token = localStorage.getItem(TOKEN_KEY);
    try {
      await axios.post(
        `${NOTES_URL}/patient/${id}`,
        { note: newNote },
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      );
      setNewNote("");
      await loadNotes();
    } catch (err) {
      console.error("Error saving note:", err);
      setNotesError("Failed to save note.");
    } finally {
      setSavingNote(false);
    }
  };

  useEffect(() => {
    loadPatient();
    loadRisk();
  }, [loadPatient, loadRisk]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p className="text-danger">{error}</p>;

  const riskBadgeClass =
    {
      NONE: "bg-success",
      BORDERLINE: "bg-warning text-dark",
      IN_DANGER: "bg-danger",
      EARLY_ONSET: "bg-dark",
    }[riskLevel] || "bg-secondary";

  const riskLabel =
    {
      NONE: "None",
      BORDERLINE: "Borderline",
      IN_DANGER: "In Danger",
      EARLY_ONSET: "Early Onset",
    }[riskLevel] || riskLevel;

  return (
    <div>
      <h2>Patient Details</h2>

      <div className="mb-2">
        <strong>Name:</strong> {patient.firstName} {patient.lastName}
      </div>

      <div className="mb-2">
        <strong>Date of Birth:</strong> {patient.dateOfBirth}
      </div>

      <div className="mb-2">
        <strong>Gender:</strong> {patient.gender}
      </div>

      <div className="mb-2">
        <strong>Address:</strong> {patient.address}
      </div>

      <div className="mb-3">
        <strong>Phone:</strong> {patient.phone}
      </div>

      <div className="mb-3">
        <strong>Diabetes Risk Assessment: </strong>
        {riskLoading && <span className="text-muted">Loading…</span>}
        {riskError && <span className="text-danger">{riskError}</span>}
        {!riskLoading && !riskError && riskLevel && (
          <span className={`badge ${riskBadgeClass} ms-1`}>{riskLabel}</span>
        )}
      </div>

      <Link to={`/patients/edit/${id}`} className="btn btn-warning me-2">
        Edit
      </Link>

      <button className="btn btn-info me-2" onClick={handleOpenNotes}>
        Notes
      </button>

      <Link to="/patients" className="btn btn-secondary">
        Back
      </Link>

      {/* Notes Modal */}
      {showNotes && (
        <div
          className="modal d-block"
          style={{ backgroundColor: "rgba(0,0,0,0.5)" }}
          onClick={(e) => {
            if (e.target === e.currentTarget) setShowNotes(false);
          }}
        >
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  Notes — {patient.firstName} {patient.lastName}
                </h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setShowNotes(false)}
                />
              </div>

              <div className="modal-body">
                {notesLoading && <p>Loading notes…</p>}
                {notesError && <p className="text-danger">{notesError}</p>}

                {!notesLoading && notes.length === 0 && (
                  <p className="text-muted">No notes yet.</p>
                )}

                {notes.map((n) => (
                  <div key={n.id} className="border rounded p-2 mb-2">
                    <div className="text-muted small mb-1">
                      {n.createdAt
                        ? new Date(n.createdAt).toLocaleString()
                        : ""}
                    </div>
                    <div>{n.note}</div>
                  </div>
                ))}

                <hr />
                <form onSubmit={handleAddNote}>
                  <label className="form-label fw-semibold">Add a note</label>
                  <textarea
                    className="form-control mb-2"
                    rows={3}
                    value={newNote}
                    onChange={(e) => setNewNote(e.target.value)}
                    placeholder="Type note here…"
                  />
                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={savingNote || !newNote.trim()}
                  >
                    {savingNote ? "Saving…" : "Save Note"}
                  </button>
                </form>
              </div>

              <div className="modal-footer">
                <button
                  className="btn btn-secondary"
                  onClick={() => setShowNotes(false)}
                >
                  Close
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default PatientDetail;

db = db.getSiblingDB("Cluster0");

db.patient_notes.insertMany([
  // Patient 12 - TestNone
  {
    patientId: 12,
    note: 'The patient states that they "feel very well." Weight is equal to or below what is recommended.',
    createdAt: new Date("2024-01-01T10:00:00"),
  },
  // Patient 13 - TestBorderline
  {
    patientId: 13,
    note: "The patient states that they feel a lot of stress at work. They also complain that their hearing has been abnormal lately.",
    createdAt: new Date("2024-01-02T10:00:00"),
  },
  {
    patientId: 13,
    note: "The patient states that they had a reaction to medication in the past three months. They also note that their hearing continues to be abnormal.",
    createdAt: new Date("2024-01-03T10:00:00"),
  },
  // Patient 14 - TestInDanger
  {
    patientId: 14,
    note: "The patient states that they have recently started smoking.",
    createdAt: new Date("2024-01-04T10:00:00"),
  },
  {
    patientId: 14,
    note: "The patient states that they used to smoke but that they quit smoking last year. They also complain of abnormal sleep apnea episodes. Laboratory tests indicate a high LDL cholesterol level.",
    createdAt: new Date("2024-01-05T10:00:00"),
  },
  // Patient 15 - TestEarlyOnset
  {
    patientId: 15,
    note: "The patient states that it has become difficult for them to climb stairs. They also complain of shortness of breath. Laboratory tests indicate elevated antibody levels. Reaction to medication.",
    createdAt: new Date("2024-01-06T10:00:00"),
  },
  {
    patientId: 15,
    note: "The patient states that they experience back pain when sitting for long periods.",
    createdAt: new Date("2024-01-07T10:00:00"),
  },
  {
    patientId: 15,
    note: "The patient states that they have recently started smoking. Hemoglobin A1C is above the recommended level.",
    createdAt: new Date("2024-01-08T10:00:00"),
  },
  {
    patientId: 15,
    note: "Height, Weight, Cholesterol, Dizziness, and Reaction to medication.",
    createdAt: new Date("2024-01-09T10:00:00"),
  },
]);

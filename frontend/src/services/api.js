// src/services/api.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add request interceptor for auth tokens if needed
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

export const patientService = {
  /**
   * GET /patients
   * Optional filter: ?lastName=Smith
   */
  getAll: (lastName) =>
    api.get('/patients', {
      params: lastName ? { lastName } : {}
    }),

  /**
   * GET /patients/{id}
   */
  getById: (id) =>
    api.get(`/patients/${id}`),

  /**
   * POST /patients
   */
  create: (data) =>
    api.post('/patients', data),

  /**
   * PUT /patients/{id}
   */
  update: (id, data) =>
    api.put(`/patients/${id}`, data)
};

export default api;
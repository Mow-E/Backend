import axios from 'axios';

// Création d'une instance Axios avec la configuration de base
const api = axios.create({
  baseURL: import.meta.env.VITE_API_HOST, // L'URL de base de votre API
});

export function setApiToken(token) {
  api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}


export default api;
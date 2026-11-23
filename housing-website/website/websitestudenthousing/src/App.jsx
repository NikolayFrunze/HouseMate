import { useEffect, useState } from "react";
import ListingCard from "./components/ListingCard";
import "./App.css";

export default function App() {
  const [listings, setListings] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [maxRent, setMaxRent] = useState("");
  const [rooms, setRooms] = useState("");

  useEffect(() => {
    const params = new URLSearchParams();
    if (maxRent) params.append("maxRent", maxRent);
    if (rooms) params.append("rooms", rooms);

    setLoading(true);
    fetch(`http://localhost:8080/api/listings?${params}`)
      .then((res) => res.json())
      .then((data) => {
        setListings(data);
        setError("");
      })
      .catch(() => setError("Could not load listings. Is the backend running?"))
      .finally(() => setLoading(false));
  }, [maxRent, rooms]);

  return (
    <div className="page">
      <h1>Student Housing</h1>

      <div className="filters">
        <input
          type="number"
          placeholder="Max rent (€ /month)"
          value={maxRent}
          onChange={(e) => setMaxRent(e.target.value)}
        />
        <select value={rooms} onChange={(e) => setRooms(e.target.value)}>
          <option value="">Any number of rooms</option>
          <option value="1">1 room</option>
          <option value="2">2 rooms</option>
          <option value="3">3 rooms</option>
          <option value="4">4 rooms</option>
        </select>
      </div>

      {error && <p>{error}</p>}
      {!loading && !error && listings.length === 0 && (
        <p>No listings match your filters.</p>
      )}

      <div className="grid">
        {listings.map((l) => (
          <ListingCard key={l.id} listing={l} />
        ))}
      </div>
    </div>
  );
}
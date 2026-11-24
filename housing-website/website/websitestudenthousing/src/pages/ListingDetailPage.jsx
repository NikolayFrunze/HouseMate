import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

export default function ListingDetailPage() {
  const { id } = useParams();
  const [listing, setListing] = useState(null);
  const [status, setStatus] = useState("loading"); // loading, ok, notfound, error

  useEffect(() => {
    setStatus("loading");
    fetch(`http://localhost:8080/api/listings/${id}`)
      .then((res) => {
        if (res.status === 404) {
          setStatus("notfound");
          return null;
        }
        if (!res.ok) throw new Error("bad response");
        return res.json();
      })
      .then((data) => {
        if (data) {
          setListing(data);
          setStatus("ok");
        }
      })
      .catch(() => setStatus("error"));
  }, [id]);

  if (status === "loading") return <p>Loading...</p>;
  if (status === "notfound") return <p>This listing does not exist. <Link to="/">Back to listings</Link></p>;
  if (status === "error") return <p>Could not load the listing. Is the backend running?</p>;

  return (
    <div className="detail">
      <Link to="/">← Back to listings</Link>
      {listing.imageUrl && <img src={listing.imageUrl} alt={listing.name} />}
      <h1>{listing.name}</h1>
      <p className="address">{listing.address}</p>
      <p className="rent">€{listing.rent} / month</p>
      <p>{listing.rooms} room(s)</p>
    </div>
  );
}
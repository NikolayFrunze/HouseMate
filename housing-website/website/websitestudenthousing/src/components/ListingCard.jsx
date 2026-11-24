import { Link } from "react-router-dom";
export default function ListingCard({ listing }) {
{/* Use <Link to="..."> instead of <a href="...">. A plain <a> reloads the whole page, and Link only swaps the component. */}
  return (
    <Link to={`/listings/${listing.id}`} className="card-link">
      <div className="card">
        {listing.imageUrl && <img src={listing.imageUrl} alt={listing.name} />}
        <div className="card-body">
          <h3>{listing.name}</h3>
          <p className="address">{listing.address}</p>
          <p className="rent">€{listing.rent} / month</p>
          <p>{listing.rooms} room(s)</p>
        </div>
      </div>
    </Link>
  );
}
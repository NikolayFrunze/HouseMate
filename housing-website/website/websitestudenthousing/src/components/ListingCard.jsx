export default function ListingCard({ listing }) {
  return (
    <div className="card">
      {listing.imageUrl && <img src={listing.imageUrl} alt={listing.name} />}
      <div className="card-body">
        <h3>{listing.name}</h3>
        <p className="address">{listing.address}</p>
        <p className="rent">€{listing.rent}/month</p>
        <p>{listing.rooms} room(s)</p>
      </div>
    </div>
  );
}

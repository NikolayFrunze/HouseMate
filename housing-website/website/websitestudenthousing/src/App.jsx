import { Link, Outlet } from "react-router-dom";
import "./App.css";

export default function App() {
  return (
    <div>
      <nav className="navbar">
        <Link to="/" className="brand">Student Housing</Link>
      </nav>
      <div className="page">
        <Outlet />
      </div>
    </div>
  );
}
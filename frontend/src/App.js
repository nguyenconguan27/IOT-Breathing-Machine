import Home from "./pages/Home";
import "./App.css";
import { Route, Routes } from "react-router-dom";
import Detail from "./pages/Detail";
import AddPantient from "./pages/AddPantient";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function App() {
  return (
    <div>
      <ToastContainer autoClose={4000} />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/detail/:id_machine" element={<Detail />} />
        <Route path="/add/:id_machine" element={<AddPantient />} />
      </Routes>
    </div>
  );
}

export default App;

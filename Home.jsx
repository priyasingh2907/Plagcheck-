import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const [file1, setFile1] = useState(null);
  const [file2, setFile2] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    navigate("/result");
  };

  return (
    <div className="max-w-md mx-auto bg-white p-6 rounded-xl shadow-md">
      <h1 className="text-2xl font-bold mb-4 text-center">Upload Documents</h1>
      <form onSubmit={handleSubmit}>
        <input type="file" accept=".pdf,.docx,.txt" onChange={(e) => setFile1(e.target.files[0])} className="mb-4 w-full" />
        <input type="file" accept=".pdf,.docx,.txt" onChange={(e) => setFile2(e.target.files[0])} className="mb-4 w-full" />
        <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600">
          Submit
        </button>
      </form>
    </div>
  );
};

export default Home;

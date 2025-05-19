const About = () => {
  return (
    <div className="max-w-md mx-auto bg-white p-6 rounded-xl shadow-md">
      <h2 className="text-xl font-bold mb-4">About This Tool</h2>
      <p>This plagiarism detection tool compares two documents and highlights similarities.</p>
      <ul className="mt-2 list-disc list-inside">
        <li><strong>Tech Stack:</strong> React, Tailwind, Flask, Java</li>
        <li><strong>Contributors:</strong> Divya (UI), others (Backend)</li>
      </ul>
    </div>
  );
};

export default About;

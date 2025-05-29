import React, { useState, useEffect } from 'react';
import '../styles/Research.css';

const Research = () => {

    const [patients, setPatients] = useState([]);
    const [filterPatient , setFilterPatient] = useState([]);
    const [diagnosisfilter, setDiagnosisfilter] = useState('');
    const [agefilter, setAgefilter] = useState('');

    

    const ageRanges = [
    { label: '1-10', min: 1, max: 10 },
    { label: '11-17', min: 11, max: 17 },
    { label: '18-20', min: 18, max: 20 },
    { label: '21-30', min: 21, max: 30 },
    { label: '31-40', min: 31, max: 40 },
    { label: '41-60', min: 41, max: 60 },
    { label: '>60', min: 61, max: Infinity },
  ];


    // API Placeholder --> CHECK
    // useEffect(() => {
    //     const fetchPatients = async () => {
    //         try {
    //             const response = await fetch('/api/patients');
    //             if (!response.ok) {
    //                 throw new Error('Network response was not ok');
    //             }
    //             const data = await response.json();
    //             setPatients(data);
    //         } catch (error) {
    //             console.error('Error fetching patients:', error);
    //         }
    //     };

    //     fetchPatients();
    // }, []);


    // For testing only --> Use API 
    const predefinedDiagnoses = [
    'Diagnosis 1',
    'Diagnosis 2',
    'Diagnosis 3',
    'Diagnosis 4',
    'Diagnosis 5',
    'Diagnosis 6'
  ];

    useEffect(() => {
    const examples = [
        {firstName: 'John', age: 45, diagnosis: 'Diagnosis 1'},
        {firstName: 'Mary', age: 32, diagnosis: 'Diagnosis 4'},
        {firstName: 'Alice', age: 29, diagnosis: 'Diagnosis 2'},
        {firstName: 'Bob', age: 50, diagnosis: 'Diagnosis 3'},
    ];
    setPatients(examples);
    setFilterPatient(examples);
    }, []);

    const uniqueDiagnoses = Array.from(new Set(patients.map(p => p.diagnosis)));

    const uniqueAges = Array.from(new Set(patients.map(p => p.age))).sort((a, b) => a - b);


    // const handleDiagnosisChange = (e) => {
    //     const value = e.target.value;
    //     setDiagnosisfilter(value);
    //     applyFilters(diagnosisfilter, value);
    // }

    // const handleAgeChange = (e) => {
    //     const value = e.target.value;
    //     setAgefilter(value);
    //     applyFilters(value, agefilter);
    // }

    const applyFilters = () => {
        let result = [...patients];

        if (diagnosisfilter) {
            result = result.filter(p => p.diagnosis.toLowerCase().includes(diagnosisfilter.toLowerCase()));
        }

        if (agefilter) {
            const ageRange = ageRanges.find(r => r.label === agefilter);
            if (ageRange) {
                result = result.filter(p => p.age >= ageRange.min && p.age <= ageRange.max);
            }
        }

        setFilterPatient(result);
    };
    
    const handleClearFilters = () => {
        setDiagnosisfilter('');
        setAgefilter('');
        setFilterPatient(patients);
  };

    return (
    <div className="research-container">
            <h1>Research Patients</h1>
            <h2>List of patients available for research purposes.</h2>
            <form onSubmit={e => e.preventDefault()}>
                <select value={diagnosisfilter} className= 'research-select-diagnosis' onChange={(e) => setDiagnosisfilter(e.target.value)}>
                <option value="" disabled hidden>Filter By Diagnosis</option>
                {predefinedDiagnoses.map((d, i) => (
                    <option key={i} value={d}>{d}</option>
                ))}

                </select>
                <select value={agefilter} className= 'research-select-age' onChange={(e) => setAgefilter(e.target.value)}>
                <option value="" disabled hidden>Filter By Age</option>
                {ageRanges.map((range, i) => (
                    <option key={i} value={range.label}>{range.label}</option>
                ))}
                </select>
                <button className='research-apply' onClick={applyFilters}>Apply Filters</button>
                <button className='research-clear' onClick={handleClearFilters}>Clear Filters</button>
            </form>
            <div className="research-list">
                {filterPatient.length === 0 ? (
                    <h2>No patients available</h2>
                ) : (
                <table>
                    <thead>
                        <tr>
                            <th>First Name</th>
                            <th>Age</th>
                            <th>Diagnosis</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filterPatient.map(patient => (
                        <tr>
                            <td>{patient.firstName}</td>
                            <td>{patient.age}</td>
                            <td>{patient.diagnosis}</td>
                        </tr>
                        ))}
                    </tbody>
                </table>
                )}
            </div>
    </div>
    );

};

export default Research;

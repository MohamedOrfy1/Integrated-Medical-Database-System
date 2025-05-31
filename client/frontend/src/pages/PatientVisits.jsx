import axios from 'axios';
import React, { useContext, useEffect, useState } from "react";
import { PatientContext } from '../Context/PatientContext';
export default function PatientVisits() {
  const tests = [
  {
    doctor: "Jane Cooper",
    date: "21.12.2022",
  },
  {
    doctor: "Bessie Cooper",
    date: "10.08.2022",
  }
]
  //const[tests,setTests]=useState(null);
    let{patientId}=useContext(PatientContext)
    function fetchTests(){
        axios.post(`https://religious-tammie-tamim21-353bd377.koyeb.app/doctors/getPatient`,{
            "PatientID": patientId
        })
        .then((response)=>{
            console.log(response)
            //set test to the response from the api response
        })
        .catch(()=>{

        })
    }
    useEffect(()=>{
        fetchTests()
      },[])
    return <>
    <div className="max-w-3xl mx-auto p-4 bg-gray-50 min-h-screen">
      <h2 className="text-2xl font-semibold mb-6 text-blue-400">Last Visits</h2>
      <div className="space-y-6">
        {tests.map((test) => (
          <div
            key={test.id}
            className="bg-white rounded-xl shadow-md border border-gray-200 p-5"
          >
            <div className="flex justify-between items-start mb-4">
              <div>
                <div className="text-lg font-semibold text-blue-600">
                  {test.doctor}
                  {/* <span className="text-gray-500 font-normal">
                    , {visit.specialty}
                  </span> */}
                </div>
              </div>
              <div className="text-right text-sm text-gray-600">
                <div>{test.date}</div>
              </div>
            </div>

            {/* <div className="text-sm mb-1">
              <strong>Time:</strong> {visit.time}
            </div>
            <div className="text-sm mb-3">
              <strong>Patient:</strong> {visit.patient}
            </div> */}

            <div className="flex gap-3">
              <button className="bg-blue-100 hover:bg-blue-200 text-sm px-4 py-2 rounded-md">
                View Test
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
    
    </>
}
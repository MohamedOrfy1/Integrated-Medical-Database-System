import React from 'react';
const visitData = [
  {
    doctor: "Jane Cooper",
    specialty: "Therapist",
    date: "21.12.2022",
    weekday: "Friday",
    time: "08:45 – 09:15 (30 min)",
    patient: "Brooklyn Simmons",
  },
  {
    doctor: "Bessie Cooper",
    specialty: "Dentist",
    date: "10.08.2022",
    weekday: "Thursday",
    time: "10:05 – 11:45 (1 h 40 min)",
    patient: "Brooklyn Simmons",
  },
  {
    doctor: "Wade Warren",
    specialty: "Ophthalmologist",
    date: "02.01.2022",
    weekday: "Friday",
    time: "13:00 – 13:30 (30 min)",
    patient: "Brooklyn Simmons",
  },
  {
    doctor: "Leslie Alexander",
    specialty: "Orthodontist",
    date: "03.10.2021",
    weekday: "Wednesday",
    time: "08:45 – 09:15 (30 min)",
    patient: "Brooklyn Simmons",
  },
];

export default function PatientVisits() {
    return <>
    <div className="max-w-3xl mx-auto p-4 bg-gray-50 min-h-screen">
      <h2 className="text-2xl font-semibold mb-6">Last Visits</h2>
      <div className="space-y-6">
        {visitData.map((visit, index) => (
          <div
            key={index}
            className="bg-white rounded-xl shadow-md border border-gray-200 p-5"
          >
            <div className="flex justify-between items-start mb-4">
              <div>
                <div className="text-lg font-semibold text-gray-800">
                  {visit.doctor}
                  <span className="text-gray-500 font-normal">
                    , {visit.specialty}
                  </span>
                </div>
              </div>
              <div className="text-right text-sm text-gray-600">
                <div>{visit.date}</div>
                <div className="text-xs">{visit.weekday}</div>
              </div>
            </div>

            <div className="text-sm mb-1">
              <strong>Time:</strong> {visit.time}
            </div>
            <div className="text-sm mb-3">
              <strong>Patient:</strong> {visit.patient}
            </div>

            <div className="flex gap-3">
              <button className="bg-gray-100 hover:bg-gray-200 text-sm px-4 py-2 rounded-md">
                Send Details
              </button>
              <button className="bg-gray-100 hover:bg-gray-200 text-sm px-4 py-2 rounded-md">
                Print Price
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
    
    </>
}
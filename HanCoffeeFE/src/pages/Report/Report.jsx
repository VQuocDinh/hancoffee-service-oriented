import React, { useState } from 'react';
import './Report.css';
import { toast } from 'react-toastify';
import axiosInstance from '../../common/library/query';

const Report = () => {
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [reportData, setReportData] = useState([]);

    const handleGenerateReport = async () => {
        if (!startDate || !endDate) {
            toast.error('Please select both start and end dates.');
            return;
        }

        try {
            const response = await axiosInstance.get(`/api/reports/product-sales?startDate=${startDate}&endDate=${endDate}`);
            if (response.data.success) {
                setReportData(response.data.data);
            } else {
                toast.error(response.data.message);
                setReportData([]);
            }
        } catch (error) {
            console.error('Error fetching report data:', error);
            toast.error('Failed to fetch report data');
        }
    };

    return (
        <div className="report">
            <h1>Product Sales Report</h1>
            <div className="report-form">
                <label>Start Date:</label>
                <input
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    required
                />
                <label>End Date:</label>
                <input
                    type="date"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                    required
                />
                <button className="generate-report-btn" onClick={handleGenerateReport}>
                    Generate Report
                </button>
            </div>
            <div className="report-results">
                {reportData.length > 0 ? (
                    <table>
                        <thead>
                            <tr>
                                <th>Product Name</th>
                                <th>Category</th>
                                <th>Units Sold</th>
                            </tr>
                        </thead>
                        <tbody>
                            {reportData.map((item, index) => (
                                <tr key={index}>
                                    <td>{item.productName}</td>
                                    <td>{item.category}</td>
                                    <td>{item.unitsSold}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                ) : (
                    <p>No data to display. Please generate a report.</p>
                )}
            </div>
        </div>
    );
};

export default Report;

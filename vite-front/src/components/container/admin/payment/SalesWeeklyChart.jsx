import React from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  Legend,
  ResponsiveContainer,
} from "recharts";

const SalesWeeklyChart = ({ weeklySales }) => {
  if (!weeklySales) return null;

  const days = ["월", "화", "수", "목", "금", "토", "일"];

  const chartData = days.map((day, idx) => ({
    day,
    thisWeek: weeklySales.thisWeek[idx],
    lastWeek: weeklySales.lastWeek[idx],
  }));

  return (
    <div
      style={{
        width: "100%",
        height: "100%",
        background: "#fff",
        borderRadius: "12px",
        padding: "20px",
        boxShadow: "0 2px 8px rgba(0,0,0,0.05)"
      }}
    >

      <h3 style={{ margin: "0 0 20px 0", color: "#333" }}>
        이번주/지난주 요일별 매출
      </h3>

      <ResponsiveContainer width="100%" height="100%">
        <LineChart
          data={chartData}
          margin={{ top: 30, right: 20, left: 40, bottom: 20 }} // 핵심
        >
          <CartesianGrid stroke="#ececec" vertical={false} />

          <XAxis
            dataKey="day"
            tick={{ fill: "#666" }}
            tickLine={false}
            axisLine={{ stroke: "#ddd" }}
          />

          <YAxis
            tickFormatter={(v) => `${v.toLocaleString()}원`}
            tick={{ fill: "#666" }}
            axisLine={false}
            tickLine={false}
          />

          <Tooltip
            formatter={(value) => `${value.toLocaleString()} 원`}
            contentStyle={{
              borderRadius: "8px",
              border: "1px solid #ddd",
              fontSize: "14px",
            }}
          />

          <Legend verticalAlign="top" wrapperStyle={{ paddingBottom: 20 }} />

          <Line
            type="monotone"
            dataKey="thisWeek"
            stroke="#4F6EF7"
            strokeWidth={3}
            dot={{ r: 4 }}
            activeDot={{ r: 6 }}
            name="이번주 매출"
          />

          <Line
            type="monotone"
            dataKey="lastWeek"
            stroke="#FF7C7C"
            strokeWidth={3}
            strokeDasharray="3 3"
            dot={{ r: 4 }}
            activeDot={{ r: 6 }}
            name="지난주 매출"
          />
        </LineChart>
      </ResponsiveContainer>

    </div>
  );
};

export default SalesWeeklyChart;

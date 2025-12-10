import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";


export default defineConfig({
  plugins: [
    react(),

  ],
  define: {
    global: "window",
  },
  server: {
    port: 3000,
    proxy: {
      "/api": {
        target: "http://localhost:8088", // 백엔드 주소
        changeOrigin: true,
        secure: false,
      },
      "/ws": {
        target: "http://localhost:8088", // 백엔드 주소
        changeOrigin: true,
        secure: false,
        ws: true,
      },
    },
  },
  define: {
    global: 'window'
  }
});

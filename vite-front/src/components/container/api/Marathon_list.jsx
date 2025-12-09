import React, { useEffect, useState, useCallback } from "react";
import "../../../css/api/Marathon_list.css"; 

const OPEN_WEATHER_KEY = import.meta.env.VITE_OPEN_WEATHER_KEY; 
const KAKAO_MAP_APP_KEY = import.meta.env.VITE_KAKAO_MAP_APP_KEY;

const DEFAULT_CITY_WEATHER = "Seoul";

// --- Kakao Maps SDK 로드 함수 ---
const loadKakaoMapScript = (appkey) => {
    return new Promise((resolve) => {
        if (window.kakao && window.kakao.maps) {
            resolve();
            return;
        }
        const script = document.createElement('script');
        script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${appkey}&autoload=false`;
        script.onload = () => window.kakao.maps.load(resolve);
        document.head.appendChild(script);
    });
};

// --- WeatherAdminFragment 컴포넌트 (날씨 및 지도) ---
const WeatherAdminFragment = ({ kakaoMapAppKey }) => {
    const [cityData, setCityData] = useState({ 
        name: DEFAULT_CITY_WEATHER, 
        description: '', 
        tempMin: null, 
        tempMax: null, 
        sunrise: null, 
        sunset: null, 
        iconUrl: '', 
        lat: null, 
        lon: null 
    });
    const [selectedCity, setSelectedCity] = useState(DEFAULT_CITY_WEATHER);
    const [weatherLoading, setWeatherLoading] = useState(true);

    const initKakaoMap = useCallback((lat, lon) => {
        if (!lat || !lon || !window.kakao || !window.kakao.maps) return;

        const mapContainer = document.getElementById('map');
        if (!mapContainer) return;

        const mapOption = {
            center: new window.kakao.maps.LatLng(lat, lon),
            level: 3
        };
        const map = new window.kakao.maps.Map(mapContainer, mapOption);

        const markerPosition = new window.kakao.maps.LatLng(lat, lon);
        const marker = new window.kakao.maps.Marker({ position: markerPosition });
        marker.setMap(map);
    }, []);

    const fetchWeather = useCallback(async (cityName) => {
        setWeatherLoading(true);
        try {
            if (!OPEN_WEATHER_KEY) {
                throw new Error("OpenWeather API 키가 로드되지 않았습니다.");
            }

            const url = `https://api.openweathermap.org/data/2.5/weather?q=${cityName}&appid=${OPEN_WEATHER_KEY}`;
            const res = await fetch(url);
            
            if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
            
            const weather = await res.json();

            const kToC = (k) => (k - 273.15).toFixed(2);
            const tsToTime = (ts) => new Date(ts * 1000).toLocaleTimeString();

            const newCityData = {
                name: weather.name,
                description: weather.weather[0].description,
                tempMin: kToC(weather.main.temp_min),
                tempMax: kToC(weather.main.temp_max),
                sunrise: tsToTime(weather.sys.sunrise),
                sunset: tsToTime(weather.sys.sunset),
                iconUrl: `https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png`,
                lat: weather.coord.lat,
                lon: weather.coord.lon
            };

            setCityData(newCityData);
            initKakaoMap(newCityData.lat, newCityData.lon);
            
        } catch (err) {
            console.error("날씨 조회 에러:", err);
            alert("날씨 정보를 불러올 수 없습니다.");
        } finally {
            setWeatherLoading(false);
        }
    }, [initKakaoMap]); 

    useEffect(() => {
        loadKakaoMapScript(kakaoMapAppKey)
            .then(() => {
                fetchWeather(selectedCity);
            });
    }, [selectedCity, fetchWeather, kakaoMapAppKey]); 
    
    const handleSearch = () => {
        fetchWeather(selectedCity);
    };

    const handleSelectChange = (e) => {
        setSelectedCity(e.target.value);
    };
    
    const { name, description, tempMin, tempMax, sunrise, sunset, iconUrl } = cityData;

    return (
        <div className="weather-section">
            <h2>☀️ 뛰기전 날씨 체크!!</h2>

            <div className="weather-con">

                {/* 왼쪽: 검색 & 정보 */}
                <div className="left">
                    <div className="search">
                        <select 
                            name="search" 
                            id="weather-search"
                            value={selectedCity} 
                            onChange={handleSelectChange}
                        >
                            <option value="Seoul">서울</option>
                            <option value="Busan">부산</option>
                            <option value="Gwangju">광주</option>
                            <option value="ChunCheon">춘천</option>
                        </select>
                        <button type="button" onClick={handleSearch}>검색</button>
                    </div>

                    {weatherLoading ? (
                        <p className="loading-state">날씨 정보 로딩 중...</p>
                    ) : (
                        <div className="info-box">
                            <h3>{name}의 현재 날씨</h3>
                            <div><span className="label">날씨:</span> <span className="description con">{description}</span></div>
                            <div><span className="label">최저온도:</span> <span className="temp_min con">{tempMin} °C</span></div>
                            <div><span className="label">최고온도:</span> <span className="temp_max con">{tempMax} °C</span></div>
                            <div><span className="label">해뜨는 시간:</span> <span className="sunrise con">{sunrise}</span></div>
                            <div><span className="label">해지는 시간:</span> <span className="sunset con">{sunset}</span></div>
                            {iconUrl && <div className="icon"><img src={iconUrl} alt="Weather Icon" /></div>}
                        </div>
                    )}
                </div>

                {/* 오른쪽: 지도 */}
                <div className="right">
                    <div id="map">
                        {!kakaoMapAppKey && <p className="map-error">카카오 맵 API 키 오류</p>}
                    </div>
                </div>
            </div>
        </div>
    );
};

// --- MarathonApiPage 컴포넌트 (메인) ---
const MarathonApiPage = () => {
// ... (나머지 로직은 변경 없음)
    const [marathons, setMarathons] = useState([]);
    const [page, setPage] = useState(0); 
    const [size] = useState(10); 
    const [totalPages, setTotalPages] = useState(0); 
    const [searchTerm, setSearchTerm] = useState("");
    const [currentSearch, setCurrentSearch] = useState(""); 
    const [loading, setLoading] = useState(true);

    
    const fetchMarathons = useCallback((pageNumber, search) => {
        setLoading(true);
        
        // 쿼리 파라미터 구성
        let url = `/api/marathons?page=${pageNumber}&size=${size}`;
        if (search) {
            url += `&searchTerm=${encodeURIComponent(search)}`;
        }

        fetch(url) 
            .then((res) => {
                if (!res.ok) throw new Error("네트워크 응답 실패");
                return res.json();
            })
            .then((data) => {
                setMarathons(data.content); 
                setTotalPages(data.totalPages); 
                setLoading(false);
            })
            .catch((err) => {
                console.error("마라톤 데이터 로딩 오류:", err);
                setMarathons([]);
                setTotalPages(0);
                setLoading(false);
            });
    }, [size]); 

    // 검색어 입력 핸들러
    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value);
    };

    // 검색 버튼 클릭 핸들러
    const handleSearchSubmit = () => {
        setPage(0);
        setCurrentSearch(searchTerm); 
    };

    // 페이지 변경 핸들러
    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < totalPages) {
            setPage(newPage);
        }
    };
    
    useEffect(() => {
        fetchMarathons(page, currentSearch);
    }, [page, currentSearch, fetchMarathons]);

    return (
        <div className="marathon-main-page">

    <h1 style={{ textAlign: 'center', marginBottom: '20px', color: '#1a73e8' }}>
                Today's Information 📌
            </h1>
            
            <div className="marathon-content-wrapper">
                
                {/* 1. 마라톤 목록 섹션 (왼쪽) */}
                <div className="marathon-section table-wrapper">
                    <h2>🏃‍♂️ 국내 마라톤대회 정보</h2>
                    
                    {/* 검색 입력 필드 */}
                    <div className="search-box">
                        <input
                            type="text"
                            placeholder="대회명 또는 장소 검색"
                            value={searchTerm}
                            onChange={handleSearchChange}
                            onKeyDown={(e) => {
                                if (e.key === 'Enter') handleSearchSubmit();
                            }}
                        />
                        <button onClick={handleSearchSubmit}>검색</button>
                    </div>

                    {loading ? (
                        <p style={{ textAlign: "center", padding: "20px" }}>데이터를 불러오는 중...</p>
                    ) : (
                        <>
                            <table className="marathon-table" border="1">
                                <thead>
                                    <tr>
                                        <th>대회명</th>
                                        <th>일시</th>
                                        <th>장소</th>
                                        <th>종목</th>
                                        <th>주최</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {marathons.length > 0 ? (
                                        marathons.map((m) => (
                                            <tr key={m.id}>
                                                <td>{m.name}</td>
                                                <td>{m.date}</td>
                                                <td>{m.location}</td>
                                                <td>{m.category}</td>
                                                <td>{m.host}</td>
                                            </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan="5" style={{ textAlign: "center" }}>
                                            {currentSearch ? "검색 결과가 없습니다." : "등록된 마라톤 정보가 없습니다."}
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>

                        {/* 페이징 컨트롤 */}
                        {totalPages > 1 && (
                            <div className="pagination-controls">
                                <button 
                                    onClick={() => handlePageChange(page - 1)} 
                                    disabled={page === 0}
                                >
                                    이전
                                </button>
                                <span>
                                    {page + 1} / {totalPages} 페이지
                                </span>
                                <button 
                                    onClick={() => handlePageChange(page + 1)} 
                                    disabled={page === totalPages - 1}
                                >
                                    다음
                                </button>
                            </div>
                        )}
                    </>
                )}
                </div>

                
                {/* 2. 날씨/지도 섹션 (오른쪽) */}
                <div className="weather-fragment-wrapper">
                    <WeatherAdminFragment kakaoMapAppKey={KAKAO_MAP_APP_KEY} /> 
                </div>
            </div>
        </div>
    );
};

export default MarathonApiPage;
import * as THREE from 'three';

// Vertex Shader (기본적인 평면 쉐이더)
const vertexShader = `
    void main() {
        gl_Position = vec4( position, 1.0 );
    }
`;

// Fragment Shader (업로드하신 무지개/유체 효과)
const fragmentShader = `
    #ifdef GL_ES
    precision mediump float;
    #endif
    uniform float time;
    uniform vec2 resolution;

    const int complexity = 6;    // More points of color.
    const float fluid_speed = -1.0;  // 스피드
    const float color_intensity = 0.5;  // 명도

    vec3 hsv2rgb(vec3 c) {
    vec3 rgb = clamp( abs(mod(c.x * 6.0 + vec3(0.0, 4.0, 2.0),
                               6.0) - 3.0) - 1.0,
                      0.0,
                      1.0 );
    return c.z * mix(vec3(1.0), rgb, c.y);
}

    void main(){
        vec2 uv = (gl_FragCoord.xy * 2.0 - resolution.xy) / min(resolution.x, resolution.y);
        
        // uv OverFlow
        vec2 original = uv;
            for (int i = 1; i < complexity; i++) {
            uv.x += 1.4/float(i) * sin(float(i) * original.y + time / fluid_speed);
            uv.y += 1.4/float(i) * cos(float(i) * original.x + time / fluid_speed);
        }
     // uv 축소 (폭주 방지)
    uv *= 0.15;

      // --- 컬러 생성 ---
    vec3 col = vec3(
        0.5 + 0.5 * sin(uv.x + uv.y + time),
        0.5 + 0.5 * sin(uv.x * 1.3 + time * 0.7),
        0.5 + 0.5 * sin(uv.y * 1.7 + time * 0.4)
    );
    // --- 최소 밝기 보정 (검정 제거) ---
    col = max(col, vec3(0.2));

    
    

       
   

        
        gl_FragColor = vec4(col, 1.0);
    }
`;

export const initializeThreeScene = (container) => {
    if (!container) return;

    let camera, scene, renderer;
    let uniforms;
    let animationId;

    // 1. Scene & Camera Setup
    scene = new THREE.Scene();
    // OrthographicCamera를 사용하여 2D 쉐이더 캔버스에 적합하게 설정
    camera = new THREE.OrthographicCamera(1, 1, 1, -1, 0, 1);
    camera.position.z = 1;

    // 2. Geometry & Uniforms
    const geometry = new THREE.PlaneGeometry(2, 2);

    uniforms = {
        time: { type: "f", value: 1.0 },
        resolution: { type: "v2", value: new THREE.Vector2() }
    };

    // 3. Material (Shader)
    const material = new THREE.ShaderMaterial({
        uniforms: uniforms,
        vertexShader: vertexShader,
        fragmentShader: fragmentShader
    });

    // 4. Mesh
    const mesh = new THREE.Mesh(geometry, material);
    scene.add(mesh);

    // 5. Renderer
    renderer = new THREE.WebGLRenderer({ alpha: true }); // 투명도 지원
    renderer.setPixelRatio(window.devicePixelRatio);

    const width = container.clientWidth || window.innerWidth;
    const height = container.clientHeight || window.innerHeight;
    // 컨테이너의 크기에 맞게 설정
    renderer.setSize(width, height);


    // 초기 유니폼 설정
    uniforms.resolution.value.x = width;
    uniforms.resolution.value.y = height;

    container.appendChild(renderer.domElement);

    // 6. Resize Handler
    const onWindowResize = () => {
        if (!container) return;
        const newWidth = container.clientWidth;
        const newHeight = container.clientHeight;

        renderer.setSize(newWidth, newHeight);
        uniforms.resolution.value.x = newWidth;
        uniforms.resolution.value.y = newHeight;
    };

    window.addEventListener('resize', onWindowResize);

    // 7. Animation Loop
    const animate = () => {
        animationId = requestAnimationFrame(animate);
        uniforms.time.value += 0.05;
        renderer.render(scene, camera);
    };

    animate();

    // 8. Clean Up Function (React useEffect의 return 함수로 사용)
    return () => {
        window.removeEventListener('resize', onWindowResize);
        cancelAnimationFrame(animationId);

        if (container && renderer.domElement) {
            container.removeChild(renderer.domElement);
        }

        // 메모리 해제
        geometry.dispose();
        material.dispose();
        renderer.dispose();
    };
};
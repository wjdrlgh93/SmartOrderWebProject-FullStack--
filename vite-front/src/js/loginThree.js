import * as THREE from "three";

const vertexShader = `
    void main() {
        gl_Position = vec4( position, 1.0 );
    }
`;

const fragmentShader = `
    #ifdef GL_ES
    precision mediump float;
    #endif

    uniform float time;
    uniform vec2 resolution;

    const int complexity = 1;        // 더 복잡하게
    const float fluid_speed = 14.0;   // 약간 느리게 → 더 무겁고 강한 느낌
    const float distortion = 1.1;    // ★ 뒤틀림 강도 2배 증가

    
    vec3 hsv2rgb(vec3 c) {
        vec3 rgb = clamp(abs(mod(c.x * 6.0 + vec3(0.0, 4.0, 2.0),
        6.0) - 3.0) - 1.0,
        0.0,
        1.0);
        return c.z * mix(vec3(1.0), rgb, c.y);
    }   

    // 회전 (소용돌이 효과)
    vec2 rotate2D(vec2 p, float a) {
        float s = sin(a);
        float c = cos(a);
        return mat2(c, -s, s, c) * p;
}

void main() {
    vec2 uv = (gl_FragCoord.xy * 3.0 - resolution.xy) /
        min(resolution.x, resolution.y);

    // 시간에 따라 화면 자체가 천천히 회전 (소용돌이)
    uv = rotate2D(uv, (time) * 0.005);

    vec2 baseUv = uv;

    // -------------------------------
    //      강력한 유체 왜곡
    // -------------------------------
    for (int i = 1; i < complexity; i++) {

        float t = time / fluid_speed;

        uv.x += distortion * 0.9 / float(i) *
            sin(float(i) * baseUv.y * 3.5 + t * 2.0);

        uv.y += distortion * 0.9 / float(i) *
            cos(float(i) * baseUv.x * 3.0 + t * 2.5);

        // 유체 같은 누적 왜곡 추가
        baseUv += 0.15 * vec2(
            sin(baseUv.y * 4.0 + t),
            cos(baseUv.x * 4.0 + t)
        );
    }

    // UV 축소 (너무 튀지 않게)
    uv *= 0.75;

    // -------------------------------
    //   네온 블루 계열 생성
    // -------------------------------

    // 파란색 계열 여러 hue 섞기
    float hue1 = 0.50 + 0.05 * sin(time * 0.4 + uv.x * 3.0);
    float hue2 = 0.52 + 0.11 * sin(time * 0.7 + uv.y * 4.0);
    float hue3 = 0.82 + 0.08 * sin(time * 0.5 + uv.x * uv.y * 4.0);

    float hue = (hue1 + hue2 + hue3) / 3.0;

    vec3 neonBlue = hsv2rgb(vec3(hue, 1.0, 1.0));

    neonBlue = max(neonBlue, vec3(0.2));

    gl_FragColor = vec4(neonBlue, 1.0);
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
    resolution: { type: "v2", value: new THREE.Vector2() },
  };

  // 3. Material (Shader)
  const material = new THREE.ShaderMaterial({
    uniforms: uniforms,
    vertexShader: vertexShader,
    fragmentShader: fragmentShader,
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

  window.addEventListener("resize", onWindowResize);

  // 7. Animation Loop
  const animate = () => {
    animationId = requestAnimationFrame(animate);
    uniforms.time.value += 0.02;
    renderer.render(scene, camera);
  };

  animate();

  // 8. Clean Up Function (React useEffect의 return 함수로 사용)
  return () => {
    window.removeEventListener("resize", onWindowResize);
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

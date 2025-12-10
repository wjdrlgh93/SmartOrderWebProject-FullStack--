import * as THREE from 'three';

export function initializeThreeScene(containerElement) {
    let camera, scene, renderer, cube;
    let animationFrameId; // requestAnimationFrame ID를 저장합니다.

    const width = containerElement.clientWidth;
    const height = containerElement.clientHeight;


    scene = new THREE.Scene();
    scene.background = new THREE.Color(0xE16E01);


    camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 1000);
    camera.position.z = 3; 


    renderer = new THREE.WebGLRenderer({ antialias:true, alpha: true});
    renderer.setSize(width, height);


    containerElement.appendChild(renderer.domElement);


    const geometry = new THREE.BoxGeometry(2,2,2);
    const material = new THREE.MeshStandardMaterial({
        color: 0xd1982e,
        metalness: 0.6,
        roughness: 0.5,
        transparent: true,
        opacity: 0.3,
    });
    cube = new THREE.Mesh(geometry, material);
    scene.add(cube);


    const ambientLight = new THREE.AmbientLight(0xffffff, 0.7);
    scene.add(ambientLight);

    const directionLight = new THREE.DirectionalLight(0xffffff, 1);
    directionLight.position.set(2,1,2).normalize();
    scene.add(directionLight);

    const directionLight2 = new THREE.DirectionalLight(0x00ffff, 1);
    directionLight2.position.set(2,-2,-2).normalize();
    scene.add(directionLight2);


    const animate = () => {
        animationFrameId = requestAnimationFrame(animate);

        if(cube){
            cube.rotation.x += 0.038;
            cube.rotation.y += 0.028;
        }

        renderer.render(scene, camera);
    };


    const onWindowResize = () => {
        const currentWidth = containerElement.clientWidth;
        const currentHeight = containerElement.clientHeight;

        camera.aspect = currentWidth / currentHeight;
        camera.updateProjectionMatrix();
        
        renderer.setSize(currentWidth, currentHeight);
        renderer.render(scene, camera);
    };

    window.addEventListener('resize', onWindowResize, false);
    animate();


    return () => {
        cancelAnimationFrame(animationFrameId);
        window.removeEventListener('resize', onWindowResize, false);

        if (renderer.domElement.parentNode === containerElement) {
             containerElement.removeChild(renderer.domElement);
        }

        scene.traverse((object) => {
            if (!object.isMesh) return;
            object.geometry.dispose();
            object.material.dispose();
        });
        renderer.dispose();
    };
}
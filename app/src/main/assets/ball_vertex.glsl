#version 300 es
uniform mat4 uMVPMatrix;
uniform mat4 uObjMatrix;
uniform vec3 uLightLocation;
uniform vec3 uCameraLocation;
in vec3 aPosition;
out vec3 vPosition;
out vec4 vAmbient;
out vec4 vDiffuse;
out vec4 vSpecular;

void diffuseStrength(in vec3 normal, in vec3 lightLocation, in vec4 lightDiffuse, inout vec4 diffuse){
    //注意法向量添加的最后一个分量为0，以目前的理解，原因可能是法向量平移没有意义
    vec3 newNormal = normalize((uObjMatrix * vec4(normal,0)).xyz);
    vec3 light = normalize(lightLocation - (uObjMatrix * vec4(aPosition,1)).xyz);
    float reflection = max(0.0, dot(newNormal, light));
    diffuse = lightDiffuse * reflection;
}

void specularStrength(in vec3 normal, in vec3 lightLocation, in vec4 lightSpecular, inout vec4 specular){
    vec3 newNormal = normalize((uObjMatrix * vec4(normal, 0)).xyz);
    vec3 newPosition = (uObjMatrix * vec4(aPosition, 1)).xyz;
    vec3 light = normalize(lightLocation - newPosition);
    vec3 camera = normalize(uCameraLocation - newPosition);
    vec3 halfVector = normalize(light + camera);
    float shininess = 25.0;
    float reflection = max(0.0, pow(dot(halfVector, newNormal), shininess));
    specular = lightSpecular * reflection;
}

void main(){
    gl_Position = uMVPMatrix * vec4(aPosition, 1);
    vPosition = aPosition;
    vAmbient = vec4(0.2,0.2,0.2,1);
    diffuseStrength(aPosition, uLightLocation, vec4(0.8,0.8,0.8,1.0), vDiffuse);
    specularStrength(aPosition, uLightLocation, vec4(0.8,0.8,0.8,1.0), vSpecular);
}
#version 300 es
precision mediump float;
uniform float uRadius;
in vec3 vPosition;
in vec4 vAmbient;
in vec4 vDiffuse;
in vec4 vSpecular;
out vec4 fragColor;

void main(){
    vec3 color;
    float n = 8.0;
    float span = 2.0*uRadius/n;
    int i = int((vPosition.x + uRadius)/span);
    int j = int((vPosition.y + uRadius)/span);
    int k = int((vPosition.z + uRadius)/span);
    if(mod(float(i+j+k),2.0)==1.0){
        color = vec3(0.678,0.231,0.129);
    }else{
        color = vec3(1,1,1);
    }
    fragColor = vec4(color,0) * (vAmbient + vDiffuse + vSpecular);
}
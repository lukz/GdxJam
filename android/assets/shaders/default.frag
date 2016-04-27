// no define stuff, we dont care about that for testing
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
  vec4 color = texture2D(u_texture, v_texCoords);
    color.rgb = vec3(1.0);
//  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
  gl_FragColor = color;
}

// GLSL言語(OpenGLシェーディング言語)

// 使用しているGLSL言語のバージョン
#version 330

// このシェーダーの入力形式
// 位置0から開始して、3つの属性で構成されるベクトルを受け取る
layout (location =0) in vec3 position;

void main()
{
    // 次元を更に一つ追加しないと、後々余分な次元がないと、操作の一部を組み合わせることができない
    gl_Position = vec4(position, 1.0);
}
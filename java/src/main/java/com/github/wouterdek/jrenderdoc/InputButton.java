package com.github.wouterdek.jrenderdoc;

public enum InputButton {
	// '0' - '9' matches ASCII values
	KEY_0(0x30),
	KEY_1(0x31),
	KEY_2(0x32),
	KEY_3(0x33),
	KEY_4(0x34),
	KEY_5(0x35),
	KEY_6(0x36),
	KEY_7(0x37),
	KEY_8(0x38),
	KEY_9(0x39),

	// 'A' - 'Z' matches ASCII values
	KEY_A(0x41),
	KEY_B(0x42),
	KEY_C(0x43),
	KEY_D(0x44),
	KEY_E(0x45),
	KEY_F(0x46),
	KEY_G(0x47),
	KEY_H(0x48),
	KEY_I(0x49),
	KEY_J(0x4A),
	KEY_K(0x4B),
	KEY_L(0x4C),
	KEY_M(0x4D),
	KEY_N(0x4E),
	KEY_O(0x4F),
	KEY_P(0x50),
	KEY_Q(0x51),
	KEY_R(0x52),
	KEY_S(0x53),
	KEY_T(0x54),
	KEY_U(0x55),
	KEY_V(0x56),
	KEY_W(0x57),
	KEY_X(0x58),
	KEY_Y(0x59),
	KEY_Z(0x5A),

	// leave the rest of the ASCII range free
	// in case we want to use it later
	KEY_NONPRINTABLE(0x100);

	/*KEY_DIVIDE,
	KEY_MULTIPLY,
	KEY_SUBTRACT,
	KEY_PLUS,

	KEY_F1,
	KEY_F2,
	KEY_F3,
	KEY_F4,
	KEY_F5,
	KEY_F6,
	KEY_F7,
	KEY_F8,
	KEY_F9,
	KEY_F10,
	KEY_F11,
	KEY_F12,

	KEY_HOME,
	KEY_END,
	KEY_INSERT,
	KEY_DELETE,
	KEY_PAGEUP,
	KEY_PAGEDN,

	KEY_BACKSPACE,
	KEY_TAB,
	KEY_PRTSCRN,
	KEY_PAUSE,

	KEY_MAX;*/

	private final int intValue;
	InputButton(int intValue){
		this.intValue = intValue;
	}

	public int getIntValue(){
		return intValue;
	}
}
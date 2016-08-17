package org.strangeforest.tcb.dataload

import java.util.*

class SetScore {

	short w_games, l_games
	Short w_tb_pt, l_tb_pt

	boolean equals(o) {
		if (!(o instanceof SetScore)) false
		SetScore score = (SetScore)o
		w_games == score.w_games && l_games == score.l_games && w_tb_pt == score.w_tb_pt && l_tb_pt == score.l_tb_pt
	}

	int hashCode() {
		Objects.hash(w_games, l_games, w_tb_pt, l_tb_pt)
	}

	String toString() {
		StringBuilder sb = new StringBuilder("SetScore{")
		sb.append("w_games=").append(w_games)
		sb.append(", l_games=").append(l_games)
		if (w_tb_pt != null)
			sb.append(", w_tb_pt=").append(w_tb_pt)
		if (l_tb_pt != null)
			sb.append(", l_tb_pt=").append(l_tb_pt)
		sb.append('}')
		sb.toString()
	}
}

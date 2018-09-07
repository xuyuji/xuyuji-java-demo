package xuyuji.demo.algorithms.problems.exam;

public class Exam {

	private static final int TOTAL_TIME = 120;

	public int exec(int[][] questions) {
		if (questions == null || questions.length == 0) {
			return 0;
		}

		int[][] dp = new int[questions.length][TOTAL_TIME + 1];
		int p1 = questions[0][0]; // 部分时间
		int a1 = questions[0][1]; // 部分得分
		int q1 = questions[0][2]; // 全部时间
		int b1 = questions[0][3]; // 全部得分
		for (int i = 1; i <= TOTAL_TIME; i++) {
			if (i < p1 && i < q1) {
				dp[0][i] = 0;
			} else if (i < q1) {
				dp[0][i] = a1;
			} else {
				dp[0][i] = b1;
			}
		}

		for (int n = 1; n < questions.length; n++) {
			int pn = questions[n][0]; // 部分时间
			int an = questions[n][1]; // 部分得分
			int qn = questions[n][2]; // 全部时间
			int bn = questions[n][3]; // 全部得分
			for (int i = 1; i <= TOTAL_TIME; i++) {
				if (i < pn && i < qn) {
				    //当前时间本题无法得分，取上一轮得分
					dp[n][i] = dp[n - 1][i];
				} else if (i < qn) {
				    //当前时间可以完成本题部分解答，判断部分解答、不解答哪个得分更多
					dp[n][i] = Math.max(dp[n - 1][i - pn] + an, dp[n - 1][i]);
				} else {
				    //当前时间可以完成本题完整解答，判断本题部分解答、完整解答、不解答哪个得分更多
					dp[n][i] = Math.max(Math.max(dp[n - 1][i - pn] + an, dp[n - 1][i - qn] + bn), dp[n - 1][i]);
				}
			}
		}

		//矩阵右下角顶点即是可得最高分
		return dp[questions.length - 1][TOTAL_TIME];
	}

	public static void main(String[] args) {
		System.out.println(new Exam()
				.exec(new int[][] { { 20, 20, 100, 60 }, { 50, 30, 80, 55 }, { 100, 60, 110, 88 }, { 5, 3, 10, 6 } }));
		System.out.println(new Exam().exec(new int[][] { { 50, 50, 100, 60 }, { 50, 30, 80, 45 } }));
	}
}

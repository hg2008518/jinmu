package com.jmz.uitl;

/**
 * 枚举订单状态
 * 
 * @author Administrator
 * 
 */
public enum OrderState {
	None {
		public String getName() {
			return "空";
		}

		public int getId() {
			return 0;
		}
	},
	RefundClosed {
		public String getName() {
			return "退款关闭";
		}

		public int getId() {
			return 0;
		}
	},
	WaitSellerConfirmGoods {
		public String getName() {
			return "等待卖家确认收货";
		}

		public int getId() {
			return 2;
		}
	},
	WaitBuyerPay {
		public String getName() {
			return "等待付款";
		}

		public int getId() {
			return 3;
		}
	},
	SellerRefuseBuyer {
		public String getName() {
			return "卖家拒绝退款";
		}

		public int getId() {
			return 4;
		}
	},
	RefundSuccess {
		public String getName() {
			return "退款成功";
		}

		public int getId() {
			return 4;
		}
	},
	WaitSellerSendGoods {
		public String getName() {
			return "等待发货";
		}

		public int getId() {
			return 5;
		}
	},
	WaitBuyerConfirmGoods {
		public String getName() {
			return "确认收货";
		}

		public int getId() {
			return 6;
		}
	},
	TradeFinished {
		public String getName() {
			return "交易完成";
		}

		public int getId() {
			return 7;
		}
	},
	TradeClosed {
		public String getName() {
			return "交易关闭";
		}

		public int getId() {
			return 8;
		}
	},
	TradeClosedBySystem {
		public String getName() {
			return "系统关闭";
		}

		public int getId() {
			return 9;
		}
	},
	WaitSellerAgree {
		public String getName() {
			return "等待卖家同意退款";
		}

		public int getId() {
			return 10;
		}
	},
	WaitBuyerReturnGoods {
		public String getName() {
			return "等待买家返回货物";
		}

		public int getId() {
			return 11;
		}
	};

	public abstract String getName();

	public abstract int getId();
}

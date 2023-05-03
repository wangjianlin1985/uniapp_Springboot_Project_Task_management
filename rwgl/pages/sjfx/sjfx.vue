<template>
	<view class="container">
		<block v-for="(item, index) in arr" :key="index">
			<view class="bgwh qiun-title-bar tc">
				<view class="f30 g6">{{item.title}}</view>
			</view>
			<view class="bgwh">
				<uCharts :scroll="item.opts.enableScroll" :show="canvas" :canvasId="item.id" :chartType="item.chartType"
					:extraType="item.extraType" :cWidth="cWidth" :cHeight="cHeight" :opts="item.opts" :ref="item.id" />
			</view>
		</block>
	</view>
</template>

<script>
	import uCharts from '@/components/leiqch-ucharts/u-charts.vue';
	export default {
		components: {
			uCharts
		},
		data() {
			return {
				canvas: true,
				cWidth: '',
				cHeight: '',
				arr: [],
				glData: {},
				yxjData: {},
				ztData: {}
			}
		},
		onLoad(e) {
			this.getYxjData()
			this.getBarData()
			this.getZtData()
			setTimeout(() => {
				this.init()
			}, 300)
		},
		methods: {
			init() {
				this.cWidth = uni.upx2px(750);
				this.cHeight = uni.upx2px(400);
				this.getServerData()
			},
			getBarData() {
				this.http.post('/openapi/chart/gl').then(res => {
					this.glData = {
						extra: {
							column: {
								type: 'group'
							}
						},
						type: "column",
						enableScroll: false,
						yAxis: {
						   min: 0,
						   max: 5,
						   format: val => {
							return val.toFixed(0)
						   }
						},
						categories: ['总项目', '我的项目', '总任务', '我的任务'],
						series: [{
							name: '数量',
							data: [res.data.countProject, res.data.countProjectJoin, res.data.countTaskTotal, res.data.countTask],
							type: 'column'
						}]
					}
				})
				console.log(this.glData)
			},
			// 任务优先级
			getYxjData() {
				this.http.post('/openapi/chart/yxj').then(res => {
					var arr = []
					Object.keys(res.data).forEach((key) => {
						arr.push({
							name: key,
							data: res.data[key],
							type: 'pie'
						})
					})
					this.yxjData = {
						extra: {
							pie: {
								type: 'group'
							}
						},
						type: "pie",
						series: arr
					}
				})
			},
			// 任务状态
			getZtData() {
				this.http.post('/openapi/chart/status').then(res => {
					// this.init_Zt(res.data)
					this.ztData = {
						extra: {
							pie: {
								type: 'group'
							}
						},
						type: "pie",
						series: [{
							name: '进行中',
							data: res.data.statusTodo,
							type: 'pie'
						}, {
							name: '正常完成',
							data: res.data.statusOk,
							type: 'pie'
						}, {
							name: '逾期',
							data: res.data.statusLate,
							type: 'pie'
						}, {
							name: '不能完成',
							data: res.data.statusNo,
							type: 'pie'
						}]
					}
				})
			},
			getServerData() {
				var serverData = [{
						title: '任务数据概览',
						opts: this.glData,
						chartType: "column",
						id: "gl"
					}, {
						title: '任务优先级',
						opts: this.yxjData,
						chartType: "pie",
						id: "yxj"
					},
					{
						title: '任务完成状态',
						opts: this.ztData,
						chartType: "pie",
						id: "zt"
					}
				];
				this.arr = serverData
			}
		}
	}
</script>

<style scoped>
	page {
		display: block;
		height: 100%;
		background-color: #efeff4;
	}

	.fl-row {
		display: flex;
		flex-direction: row;
	}

	.bgwh {
		background-color: #FFFFFF;
	}

	/*白色*/
	.g6 {
		color: #666;
	}

	/*浅黑*/
	.tc {
		text-align: center;
	}

	.f30 {
		font-size: 30upx;
	}

	.p10 {
		padding: 10upx;
	}

	.mt10 {
		margin-top: 10upx;
	}

	.mb10 {
		margin-bottom: 10upx;
	}

	.ml10 {
		margin-left: 10upx;
	}

	.mr10 {
		margin-right: 10upx;
	}

	.qiun-title-bar {
		width: 100%;
		padding: 10upx 2%;
		flex-wrap: nowrap;
	}

	.container {
		box-sizing: border-box;
		height: 100%;
		background-color: #efeff4;
	}

	.content {
		width: 750upx;
		height: calc(100% - 100upx);
		background-color: #efeff4;
	}

	.title {
		line-height: 80upx;
		background-color: #eee;
		text-indent: 20upx;
		font-size: 30upx;
		color: #000000;
		letter-spacing: 1px;
	}

	.i-cube {
		width: 12upx;
		height: 60upx;
		background-color: #007AFF;
		margin-right: 20upx;
	}

	.s-btn {
		font-size: 28upx;
		background-color: transparent;
		color: #f6a121;
		line-height: 1.8;
		padding-left: 20upx;
		padding-right: 20upx;
	}

	.s-btn:after {
		border: 2upx solid #8799A3;
	}

	.s-btn-hover {
		background-color: #f6a121;
		color: #FFFFFF;
	}

	.s-btn1 {
		font-size: 28upx;
		background-color: transparent;
		color: #f6a121;
		line-height: 1.8;
		padding-left: 20upx;
		padding-right: 20upx;
	}

	.s-btn1:after {
		border: 2upx solid #8799A3;
	}

	.s-btn1-hover {
		background-color: #007AFF;
		color: #FFFFFF;
	}
</style>

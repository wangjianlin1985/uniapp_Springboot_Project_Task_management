<template>
	<view>
		<view class="cu-list grid" :class="['col-2']">
			<view class="cu-item" @tap="toMbgl()">
				<view class="icon iconfont icon-ketiguanli base_fontcolor  bigsize " ></view>
				<text>模板管理</text>
			</view>
			<view class="cu-item"  @tap="toWdxm()">
				<view class="icon iconfont icon-lunwenguanli base_fontcolor bigsize" ></view>
				<text>我的项目</text>
			</view>
			<view class="cu-item"  @tap="toXmhsz()">
				<view class="icon iconfont icon-lunwenguanli base_fontcolor bigsize" ></view>
				<text>项目回收站</text>
			</view>
			<view class="cu-item" @tap="toXmyq()">
				<view class="icon iconfont icon-peixun base_fontcolor bigsize" ></view>
				<text>项目邀请</text>
			</view>
			
			<view class="cu-item" @tap="toWjrd()" >
				<view class="icon iconfont icon-keyanchengguohuojiang base_fontcolor bigsize" ></view>
				<text>我加入的</text>
			</view>
			
			<view class="cu-item" @tap="toBqgl()" >
				<view class="icon iconfont icon-zhengshu-copy base_fontcolor bigsize" ></view>
				<text>标签管理</text>
			</view>
			
			<view class="cu-item" @tap="toMyrw()" >
				<view class="cuIcon-check base_fontcolor bigsize" ></view>
				<text>我的任务</text>
			</view>
			
			<view class="cu-item" @tap="toSjfx()" >
				<view class="cuIcon-activity base_fontcolor bigsize" ></view>
				<text>数据分析</text>
			</view>
			
			<view class="cu-item" @tap="toGrzx()" >
				<view class="icon-jieyonglingyongshenqingbiao base_fontcolor bigsize" ></view>
				<text>个人中心</text>
			</view>
			
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			};
		},
		methods: {
			// 模板及步骤
			toMbgl() {
				uni.navigateTo({
					url: '../projecttemp/projecttemp'
				});
			},
			
			// 项目
			toWdxm(){
				uni.navigateTo({
					url: '../project/myproject'
				})
			},
			
			// 项目回收站
			toXmhsz(){
				uni.navigateTo({
					url: '../project/myprojectHuishou'
				})
			},
			
			// 项目邀请
			toXmyq(){
				uni.navigateTo({
					url: '../project/user/yaoqing/yaoqing'
				})
			},
			
			// 我加入的项目
			toWjrd(){
				uni.navigateTo({
					url: '../project/projectjoin/projectjoin'
				})
			},
			
			// 标签管理
			toBqgl(){
				uni.navigateTo({
					url: '../projectlabel/projectlabel'
				})
			},
			
			// 我的任务
			toMyrw(){
				uni.navigateTo({
					url: '../task/mytask/mytask'
				})
			},
			
			// 个人中心
			toGrzx(){
				uni.navigateTo({
					url: '../grzx/grzx'
				})
			},
			
			toSjfx(){
				uni.navigateTo({
					url: '../sjfx/sjfx'
				})
			}
			
		}
	}
</script>
<style scoped>
	.bigsize {
		font-size: xx-large;
	}
</style>
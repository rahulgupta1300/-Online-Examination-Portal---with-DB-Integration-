<template>
  <div>
    <div class="clever-category bg-img" style="background-image: url(static/img/bg-img/bg2.jpg);">
      <h3>热门课程</h3>
    </div>
    <div class="content-container">
      <div class="course-card-list">
        <transition name="fade-transform" mode="out-in" v-for="course in courseList" :key="course.id">
          <div class="single-popular-course" v-show="course.show" @click="handleStartCourse(course)">
            <img :src="course.imageUrl" :alt="course.courseName">
            <div class="course-content">
              <h4>{{ course.courseName }}</h4>
              <div class="meta d-flex align-items-center" v-if="course.college">
                <a href="#">{{ course.college }} & {{ course.major }}</a>
                <span><i class="fa fa-circle" aria-hidden="true"></i></span>
                <a href="#">{{ course.teacher }}</a>
              </div>
              <p>{{ course.simpleDesc | simpleStrFilter(20) }}</p>
            </div>
            <div class="seat-rating-fee d-flex justify-content-between">
              <div class="seat-rating h-100 d-flex align-items-center">
                <div class="seat" title="已报名学员人数">
                  <i class="el-icon-user-solid" aria-hidden="true"></i> {{course.memberCount}}
                </div>
                <div class="rating" title="难度等级">
                  <i class="el-icon-star-on" aria-hidden="true"></i> {{course.level}}
                </div>
              </div>
              <div :class="course.chargeType === 0 ? 'course-fee h-100' : 'course-charge h-100'">
                <a href="#">{{course.chargeType === 0 ? '免费' : '$' + course.chargePrice}}</a>
              </div>
            </div>
          </div>
        </transition>
        <!-- 对齐 -->
        <i v-if="courseList !== undefined && courseList.length > 0" v-for="count in (courseList.length)" :key="count"></i>
      </div>
      <el-row style="text-align: center; margin-bottom: 50px;">
        <el-col :span="24">
          <el-button v-if="!isLastPage" type="default" @click="scrollList" :loading="loading" style="margin-bottom: 100px;">加载更多</el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import { courseList } from '@/api/exam/course'
import { messageWarn, notifyFail } from '@/utils/util'
import {simpleStrFilter} from '@/filters/index'

export default {
  data () {
    return {
      total: 0,
      loading: true,
      isLastPage: false,
      query: {
        sort: 'id',
        order: ' asc',
        page: 1,
        pageSize: 6,
        courseName: '',
        status: 0
      },
      courseList: []
    }
  },
  created () {
    this.getCourseList()
  },
  methods: {
    simpleStrFilter: simpleStrFilter,
    // 加载课程列表
    getCourseList () {
      this.loading = true
      courseList(this.query).then(response => {
        const { total, isLastPage, list } = response.data.result
        this.updateCourseList(list)
        this.total = total
        this.isLastPage = isLastPage
        this.loading = false
      }).catch(() => {
        notifyFail(this, '加载数据失败！')
        this.loading = false
      })
    },
    handleStartCourse (course) {
      this.$router.push({name: 'course-details', query: {courseId: course.id}})
    },
    scrollList () {
      if (this.isLastPage) {
        messageWarn(this, '暂无更多数据！')
        return
      }
      if (this.loading) {
        messageWarn(this, '正在拼命加载！')
        return
      }
      this.loading = true
      setTimeout(() => {
        this.query.page++
        courseList(this.query).then(response => {
          const { total, isLastPage, list } = response.data.result
          this.updateCourseList(list)
          this.total = total
          this.isLastPage = isLastPage
          this.loading = false
        }).catch(() => {
          messageWarn(this, '加载数据失败！')
          this.loading = false
        })
      }, 500)
    },
    updateCourseList (list) {
      if (list === undefined || list.length === 0) {
        return list
      }
      list.forEach(item => {
        item.show = false
      })
      if (this.courseList.length === 0) {
        this.courseList = list
      } else {
        list.forEach(item => {
          let exist = false
          for (let i = 0; i < this.courseList.length; i++) {
            if (this.courseList[i].id === item.id) {
              exist = true
            }
          }
          if (!exist) {
            this.courseList.push(item)
          }
        })
      }
      for (let i = 0; i < list.length; i++) {
        setTimeout(() => {
          list[i].show = true
        }, 150 + (100 * i))
      }
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.clever-category {
  display: flex;
  width: 100%;
  height: 80px;
  position: relative;
  align-items: center;
  justify-content: center;
  -webkit-box-pack: center;
  z-index: 2;
  h3 {
    text-align: center;
    font-size: 30px;
    text-transform: uppercase;
    letter-spacing: 0.75px;
    color: #3762f0;
    margin-bottom: 0;
  }
}
.clever-category::after {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  content: '';
  background-color: rgba(215, 224, 252, 0.9);
  z-index: -1;
}
.bg-img {
  background-position: center center;
  background-size: cover;
  background-repeat: no-repeat;
}
.course-card-list {
  width: 100%;
  height: auto;
  overflow: auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  flex-direction: row;
  .single-popular-course {
    width: 30%;
    position: relative;
    margin-bottom: 40px;
    border-radius: 6px;
    box-shadow: 0 3px 8px rgba(0, 0, 0, 0.15);
    .card-item-snapshoot {
      border: 1px solid rgba(0,0,0,.15);
      background-origin: border-box;
      background-size: cover;
      background-color: #f0f0f0;
      background-position: 49% 38% ;
      height: 172px;
      display: block;
      border-radius: 6px 6px 0 0;
    }
    .card-item-detail {
      padding: 20px;
      .card-item-name {
        display: -webkit-box;
        overflow: hidden;
      }
      .card-item-course {
        --x-height-multiplier: 0.342;
        --baseline-multiplier: 0.22;
        font-weight: 300;
        font-style: normal;
        letter-spacing: 0;
        .card-item-course-detail {
          color: rgba(0,0,0,.54);
          fill: rgba(0,0,0,.54);
        }
      }
    }
  }
  >i {
    width: 30%;
    padding-right: 12px;
  }
}
</style>

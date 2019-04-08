FROM hub.chinacloud.com.cn/common/jre:1.8.0
MAINTAINER fast "fast@chinacloud.com.cn"

RUN mkdir -p /apps

COPY spring-boot-demo.jar /apps

COPY spring-boot-demo.conf /apps

CMD ["/apps/spring-boot-demo.jar", "start"]

EXPOSE 8080
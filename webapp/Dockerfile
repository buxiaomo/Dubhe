FROM docker.io/library/nginx:1.27.5-alpine

# Set working dir
WORKDIR /data

# Copy nginx config
COPY nginx.conf /etc/nginx/nginx.conf
COPY default.conf /etc/nginx/conf.d/default.conf

RUN mkdir webapp

# Copy compiled code
COPY Dubhe/webapp/dist ./webapp/dist


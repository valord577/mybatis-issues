#!/usr/bin/env bash

# Vert.x Project Manager Script.
#
#   @author valor.
#
# Usage:
#   bash vertx.sh -h
#   bash vertx.sh -s ${jar.name} [args...]
#   bash vertx.sh -k
#

function die() {
  echo -e "\033[01;31;01m $*\033[01;00;00m"
  exit 1
}

function checkDependencies() {
  if [ $# -lt 1 ]; then
    return
  fi

  for arg in $@ ; do
    which ${arg} >/dev/null 2>&1 || die "No '${arg}' command could be found in your PATH."
  done
}

function run() {
  Xmx=${1}
  Xms=${2}
  active=${3}
  shift 3

  if [ $# -lt 1 ]; then
    die "Please specify a jar file."
  fi

  checkDependencies nohup java tail

  jarName=${1}
  shift
  args=$@
  logFile="vertx.log"

  if [ ! -f "${logFile}" ]; then
    touch "${logFile}"
  fi

  # ============================================================================================
  # for jconsole or jvisualvm(No integrated on jdk9+ | https://visualvm.github.io/index.html):
  #   -Dcom.sun.management.jmxremote
  #   -Dcom.sun.management.jmxremote.port=22222
  #   -Dcom.sun.management.jmxremote.rmi.port=22222
  #   -Dcom.sun.management.jmxremote.authenticate=false
  #   -Dcom.sun.management.jmxremote.ssl=false
  # ============================================================================================
  nohup java \
    -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:55005 \
    ${Xmx} \
    ${Xms} \
    -XX:+UseZGC \
    -XX:SoftRefLRUPolicyMSPerMB=0 \
    -Djava.complier=NONE \
    -Dfile.encoding=UTF-8 \
      -jar ${jarName} ${active} ${args} > ${logFile} \
    2>&1 &

  tail -f ${logFile}
}

function close() {
  checkDependencies curl

  # ${base-package}.boot.verticle.SysVerticle
  curl --request GET \
      --url 'http://[::1]:60000/close'
}

function usage() {
  echo "Vert.x Project Manager Script."
  echo ""
  echo "usage: bash vertx.sh (arguments)    Execute this script. Arguments must be existed."
  echo ""
  echo "Arguments:"
  echo "  -h|--help    Print Help (this message) and exit.          Opposite other command."
  echo "  -s|--start   Start Vert.x Server for Prod.                Opposite other command."
  echo "  -k|--close   Close or kill Vert.x server.                 Opposite other command."
}

if [ $# -lt 1 ]; then
  die "Please use '-h' or '--help' to view the usage."
fi

case ${1} in
  -h|--help)
    usage
    ;;
  -s|--start)
    shift
    run -Xmx768m -Xms768m --active=starter $@
    ;;
  -k|--close)
    close
    ;;

  *)
    die "Please use '-h' or '--help' to view the usage."
    ;;
esac

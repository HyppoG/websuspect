#!/bin/sh
#set -x

NBPAGE=1
DFTUSER=sec3425
TMPFILE=listGitUsers.tmp
BUILDFILE=listGitUsers.build
RESULTFILE=listGitUsers.txt
 
function download_page(){
        START=$((1000*($NBPAGE-1)))
        echo "Downloading records  $START - " $(($START+1000))" "
        curl -s -u $USER:$PASSWORD "https://bitbucket.secomrecon.com/rest/api/1.0/users?limit=1000&start=$START" > $TMPFILE
}
function check_result(){
        GREPVALUE=`fgrep \"isLastPage\":true $TMPFILE`
        if [ "$GREPVALUE" = "" ]
        then
                #echo Processing next records ...
                NBPAGE=$(($NBPAGE+1))
        else
                process_results
        fi;
}
function store_records(){
        cat $TMPFILE |  awk -F: -v RS="," '$1~/"emailAddress"/ {print $2","}' >> $BUILDFILE
}
function check_creds(){
        if [ "`fgrep "Authentication failed" $TMPFILE`" != "" ]
        then
                echo "*** Failed to Authenticate ***"
                exit
        fi
}
function process_results(){
        store_records
        cat $BUILDFILE | grep @ | sort -u >> $RESULTFILE
        NBRECORDS=`cat $RESULTFILE | grep @ | wc -l`
        #rm $BUILDFILE
        rm $TMPFILE
        NBPAGE=0
 
        echo -\> $NBRECORDS records processed
        echo Git users email are listed in the file ./$RESULTFILE
        echo
}
function get_creds(){
        read -p "User ($DFTUSER) : " USER
        read -sp "Password : " PASSWORD
        echo
        if [ "$USER" = "" ]
        then
                USER=$DFTUSER
        fi
}
echo "" > $BUILDFILE
echo "GitUsers : `date`" > $RESULTFILE
clear
echo --- Get Git Users list
get_creds
echo
download_page
check_creds
check_result
while [ $NBPAGE != 0 ]
do
        download_page
        check_result
done

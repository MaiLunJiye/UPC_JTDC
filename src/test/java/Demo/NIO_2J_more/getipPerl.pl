#!/usr/bin/perl
# 给网卡添加ip地址脚本

use utf8;
use 5.010;

my $ipbase='192.168.43.';       # 网络段
my $ipstart1=150;               # 第一组地址起始
my $ipstart2=170;               # 第二组地址起始
my $wlan0 = 'wlan0';            # 第一组网卡
my $wlan1 = 'wlan1';            # 第二组网卡
my $netmask = "255.255.255.0";  # 子网掩码
my $end=9;                      # 分配数量（9 表示 分配10 个 0..9)


my @wlanip1;
my @wlanip2;

for my $index (0..$end) {
    my $ip = $ipbase.($ipstart1 + $index);
    push @wlanip1,$ip;

    system("ifconfig ".$wlan0.":".$index." ".$ip." netmask ".$netmask." up");
    $ip = $ipbase.($ipstart2 + $index);
    push @wlanip2,$ip;
    system("ifconfig ".$wlan1.":".$index." ".$ip." netmask ".$netmask." up");
}

for my $index (@wlanip1) {
    say "\"$index\",";
}

say "-----------------------";

for my $index (@wlanip2) {
    say "\"$index\",";
}

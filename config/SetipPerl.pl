#!/usr/bin/perl

use utf8;
use strict;
use 5.010;
use JSON;
use Data::Dumper;

my ( $JSONpath, $dev, $ipgroup, ) = @ARGV;

open my $Conft, '<', $JSONpath or die $!;
my $jsonString;
$jsonString .= $_ while ( $_ = <$Conft> );
close $Conft;

my $jsonObj = decode_json $jsonString;
die "can't find ipgroup $ipgroup, $!" unless $jsonObj->{$ipgroup};

my $netmask = "255.255.255.0";    # 子网掩码
my $index   = 0;
for my $ip ( @{ $jsonObj->{$ipgroup} } ) {
    system("ifconfig $dev:$index $ip netmask $netmask up");
    $index++;
    say $ip;
}


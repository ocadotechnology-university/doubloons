PGDMP                         {        	   Doubloons    15.2    15.2                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    16589 	   Doubloons    DATABASE     ~   CREATE DATABASE "Doubloons" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Polish_Poland.1250';
    DROP DATABASE "Doubloons";
                postgres    false            �            1259    16590 
   categories    TABLE     �   CREATE TABLE public.categories (
    category_id integer NOT NULL,
    category_name character varying(30) NOT NULL,
    category_description character varying(255) NOT NULL
);
    DROP TABLE public.categories;
       public         heap    postgres    false            �            1259    16666    comments    TABLE     �   CREATE TABLE public.comments (
    month_and_year character varying(7) NOT NULL,
    given_to character varying(30) NOT NULL,
    given_by character varying(30) NOT NULL,
    comment character varying(1000) NOT NULL
);
    DROP TABLE public.comments;
       public         heap    postgres    false            �            1259    16593 	   doubloons    TABLE       CREATE TABLE public.doubloons (
    doubloon_id integer NOT NULL,
    category_id integer NOT NULL,
    given_to character varying(30) NOT NULL,
    given_by character varying(30) NOT NULL,
    amount integer NOT NULL,
    month_and_year character varying(7) NOT NULL
);
    DROP TABLE public.doubloons;
       public         heap    postgres    false            �            1259    16625    team_leaders    TABLE     q   CREATE TABLE public.team_leaders (
    leader_id character varying(30) NOT NULL,
    team_id integer NOT NULL
);
     DROP TABLE public.team_leaders;
       public         heap    postgres    false            �            1259    16596    teams    TABLE     �   CREATE TABLE public.teams (
    team_id integer NOT NULL,
    team_name character varying(30) NOT NULL,
    team_description character varying(255) NOT NULL
);
    DROP TABLE public.teams;
       public         heap    postgres    false            �            1259    16599    users    TABLE       CREATE TABLE public.users (
    email character varying(30) NOT NULL,
    team_id integer,
    first_name character varying(20) NOT NULL,
    last_name character varying(20) NOT NULL,
    password character varying(255) NOT NULL,
    avatar character varying(255)
);
    DROP TABLE public.users;
       public         heap    postgres    false            y           2606    16645    categories categories_pkey 
   CONSTRAINT     a   ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (category_id);
 D   ALTER TABLE ONLY public.categories DROP CONSTRAINT categories_pkey;
       public            postgres    false    214            �           2606    16670    comments comments_pkey 
   CONSTRAINT     t   ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (month_and_year, given_to, given_by);
 @   ALTER TABLE ONLY public.comments DROP CONSTRAINT comments_pkey;
       public            postgres    false    219    219    219            {           2606    16643    doubloons doubloons_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT doubloons_pkey PRIMARY KEY (doubloon_id);
 B   ALTER TABLE ONLY public.doubloons DROP CONSTRAINT doubloons_pkey;
       public            postgres    false    215            �           2606    16641    team_leaders team_leaders_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.team_leaders
    ADD CONSTRAINT team_leaders_pkey PRIMARY KEY (leader_id, team_id);
 H   ALTER TABLE ONLY public.team_leaders DROP CONSTRAINT team_leaders_pkey;
       public            postgres    false    218    218            }           2606    16624    teams teams_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.teams
    ADD CONSTRAINT teams_pkey PRIMARY KEY (team_id);
 :   ALTER TABLE ONLY public.teams DROP CONSTRAINT teams_pkey;
       public            postgres    false    216                       2606    16622    users users_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (email);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    217            �           2606    16676    comments comments_given_by_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_given_by_fkey FOREIGN KEY (given_by) REFERENCES public.users(email);
 I   ALTER TABLE ONLY public.comments DROP CONSTRAINT comments_given_by_fkey;
       public          postgres    false    217    219    3199            �           2606    16671    comments comments_given_to_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_given_to_fkey FOREIGN KEY (given_to) REFERENCES public.users(email);
 I   ALTER TABLE ONLY public.comments DROP CONSTRAINT comments_given_to_fkey;
       public          postgres    false    219    217    3199            �           2606    16656 !   doubloons doubloons_category_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT doubloons_category_fkey FOREIGN KEY (category_id) REFERENCES public.categories(category_id) NOT VALID;
 K   ALTER TABLE ONLY public.doubloons DROP CONSTRAINT doubloons_category_fkey;
       public          postgres    false    215    214    3193            �           2606    16651 !   doubloons doubloons_given_by_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT doubloons_given_by_fkey FOREIGN KEY (given_by) REFERENCES public.users(email) NOT VALID;
 K   ALTER TABLE ONLY public.doubloons DROP CONSTRAINT doubloons_given_by_fkey;
       public          postgres    false    217    3199    215            �           2606    16646 !   doubloons doubloons_given_to_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT doubloons_given_to_fkey FOREIGN KEY (given_to) REFERENCES public.users(email) NOT VALID;
 K   ALTER TABLE ONLY public.doubloons DROP CONSTRAINT doubloons_given_to_fkey;
       public          postgres    false    217    215    3199            �           2606    16628 %   team_leaders team_leaders_leader_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.team_leaders
    ADD CONSTRAINT team_leaders_leader_fkey FOREIGN KEY (leader_id) REFERENCES public.users(email);
 O   ALTER TABLE ONLY public.team_leaders DROP CONSTRAINT team_leaders_leader_fkey;
       public          postgres    false    218    217    3199            �           2606    16633 #   team_leaders team_leaders_team_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.team_leaders
    ADD CONSTRAINT team_leaders_team_fkey FOREIGN KEY (team_id) REFERENCES public.teams(team_id);
 M   ALTER TABLE ONLY public.team_leaders DROP CONSTRAINT team_leaders_team_fkey;
       public          postgres    false    3197    216    218            �           2606    16661    users users_team_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_team_fkey FOREIGN KEY (team_id) REFERENCES public.teams(team_id) NOT VALID;
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_team_fkey;
       public          postgres    false    217    3197    216           